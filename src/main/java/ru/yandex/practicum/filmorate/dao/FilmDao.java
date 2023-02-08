package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mappers.FilmMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@Qualifier("filmDao")
public class FilmDao implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreStorage genreStorage;
    private final MPAStorage mpaStorage;

    public FilmDao(JdbcTemplate jdbcTemplate, GenreStorage genreStorage, MPAStorage mpaStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreStorage = genreStorage;
        this.mpaStorage = mpaStorage;
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "SELECT f.film_id," +
                "f.film_name," +
                "f.description," +
                "f.release_date," +
                "f.duration," +
                "f.rating_id," +
                "gnames.GENRE_ID," +
                "gnames.GENRE_NAME, " +
                "mr.rating_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa_rating AS mr ON f.rating_id = mr.rating_id " +
                "LEFT JOIN ( SELECT  " +
                "g.genre_id," +
                "gn.genre_name," +
                "g.FILM_ID " +
                "FROM genre AS g " +
                "JOIN genres AS gn ON g.genre_id = gn.genre_id ) " +
                "AS gnames  ON f.film_id = gnames.film_id ;";
        return jdbcTemplate.query(sql, new FilmMapper(genreStorage));
    }

    @Override
    public Optional<Film> addFilm(Film film) throws ValidationException {
        validation(film);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        int genId = (int) simpleJdbcInsert.executeAndReturnKey(Map.of(
                "film_name", film.getName(),
                "description", film.getDescription(),
                "rating_id", film.getMpa().getId(),
                "release_date", film.getReleaseDate(),
                "duration", film.getDuration()
        ));
        film.setId(genId);
        if (film.getGenres() != null) {
            String insertGenresTable = "INSERT INTO genre (film_id, genre_id) VALUES ( ?,? )";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertGenresTable, film.getId(), genre.getId());
            }
        }
        return findFilmById(film.getId());
    }

    @Override
    public Optional<Film> findFilmById(int id) throws NotFoundException {
        SqlRowSet row = jdbcTemplate.queryForRowSet("SELECT * from films where film_id = ?", id);
        if (row.first()){
            Film film = new Film(
                    row.getInt("film_id"),
                    row.getString("film_name"),
                    row.getString("description"),
                    genreStorage.getFilmGenres(row.getInt("film_id")),
                    mpaStorage.getRatingById(row.getInt("rating_id")),
                    row.getDate("release_date").toLocalDate(),
                    row.getInt("duration"));
            return Optional.of(film);
        } else {
            throw new NotFoundException("not found");
        }

    }

    @Override
    public void deleteFilm(Film film) {
        try {
            String delReq = "DELETE FROM films WHERE film_id = ?";
            jdbcTemplate.update(delReq, film.getId());
        }catch (Exception e){
            throw new NotFoundException("film not found!");
        }
    }

    @Override
    public Optional<Film> updateFilm(Film film) throws NotFoundException, ValidationException {
        validation(film);
            String sqlReq = "UPDATE films SET " +
                    "film_name = ?," +
                    "description = ?," +
                    "rating_id = ?," +
                    "release_date = ?," +
                    "duration = ? " +
                    "WHERE film_id = ?";
            jdbcTemplate.update(sqlReq, film.getName(), film.getDescription(), film.getMpa().getId(), film.getReleaseDate(),
                    film.getDuration(), film.getId());
            //обновление жанров
            if (film.getGenres() == null){
                jdbcTemplate.update("DELETE FROM GENRE WHERE FILM_ID =?", film.getId());
            } else {
                // чистка списка жанров перед обновлением
                jdbcTemplate.update("DELETE FROM GENRE WHERE FILM_ID =?", film.getId());
                String insertGenresTable = "MERGE INTO genre (film_id, genre_id) VALUES ( ?,? )";
                for (Genre genre: film.getGenres()) {
                    jdbcTemplate.update(insertGenresTable, film.getId(), genre.getId());
                }
            }
            return findFilmById(film.getId());

    }

    @Override
    public void deleteLike(int id, int userId) throws NotFoundException{
        if (id <0 || userId < 0){
            throw new NotFoundException("negative id");
        }
        try {
            String sqlReq = "DELETE FROM likes WHERE user_id = ? and film_id = ?";
            jdbcTemplate.update(sqlReq, userId, id);
        } catch (Exception e) {
            throw new NotFoundException("not found");
        }
    }

    @Override
    public void likeFilm(int id, int userId) {
        String sqlReq = "INSERT INTO likes (user_id, film_id) VALUES (?,?)";
        jdbcTemplate.update(sqlReq,userId,id);
    }

    @Override
    public List<Film> mostLikedFilms(int count) {
        String sqlReq = "SELECT f.film_id," +
                "f.film_name," +
                "f.description," +
                "f.release_date," +
                "f.duration," +
                "f.rating_id," +
                "gnames.GENRE_ID," +
                "gnames.GENRE_NAME, " +
                "mr.rating_name " +
                "FROM films AS f " +
                "LEFT JOIN mpa_rating AS mr ON f.rating_id = mr.rating_id " +
                "LEFT JOIN ( SELECT  " +
                "g.genre_id," +
                "gn.genre_name," +
                "g.FILM_ID " +
                "FROM genre AS g " +
                "JOIN genres AS gn ON g.genre_id = gn.genre_id ) " +
                "AS gnames  ON f.film_id = gnames.film_id " +
                "LEFT JOIN (SELECT " +
                "film_id," +
                "COUNT(user_id) AS cl " +
                "FROM likes " +
                "group by film_id " +
                ") AS l ON f.film_id = l.film_id " +
                "ORDER BY cl DESC " +
                "LIMIT ?;";
        
        if (count > 0){
            return jdbcTemplate.query(sqlReq, new FilmMapper(genreStorage), count);
        } else {
            return jdbcTemplate.query(sqlReq, new FilmMapper(genreStorage), 10);
        }
    }

    private static void validation (Film film) throws ValidationException {
        LocalDate filmsBirthday = LocalDate.parse("1895-12-28");

        boolean valid = !film.getName().isBlank() && film.getDescription().length() <= 200 &&
                film.getDuration() > 0 && film.getReleaseDate().isAfter(filmsBirthday);

        if (!valid) {
            throw new ValidationException("Bad request!!!");
        }
    }
}
