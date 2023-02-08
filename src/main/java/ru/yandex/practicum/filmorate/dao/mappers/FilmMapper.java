package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;


public class FilmMapper implements RowMapper<Film> {
    @Autowired
    private final GenreStorage genreStorage;

    public FilmMapper(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    @Override
    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
        Film film = Film.builder()
                .id(rs.getInt("film_id"))
                .name(rs.getString("film_name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(MPARating.builder()
                        .id(rs.getInt("rating_id"))
                        .name(rs.getString("rating_name"))
                        .build())
                .genres(genreStorage.getFilmGenres(rs.getInt("film_id")))
                .duration(rs.getInt("duration"))
                .build();
        return film;
    }

}
