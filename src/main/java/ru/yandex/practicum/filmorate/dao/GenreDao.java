package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mappers.GenreMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;

@Component
public class GenreDao implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public GenreDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Genre getById(int id) {
        try {
            String sqlReq = "SELECT genre_id, genre_name FROM genres WHERE genre_id = ?";

            return jdbcTemplate.queryForObject(sqlReq, new GenreMapper(), id);
        }catch (Exception e) {
            throw new NotFoundException("not found");
        }
    }

    @Override
    public Collection<Genre> getAll() {
        String sqlReq = "SELECT * FROM genres";
        return jdbcTemplate.query(sqlReq, new GenreMapper());
    }
    @Override
    public List<Genre> getFilmGenres (int id){
        String sqlReq = "Select " +
                "g.genre_id," +
                "gs.genre_name " +
                "From genre as g " +
                "Left join genres AS gs ON g.genre_id = gs.genre_id " +
                "WHERE film_id = ?";
        return jdbcTemplate.query(sqlReq, new GenreMapper(), id);
    }
}
