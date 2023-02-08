package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mappers.MPAMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.Collection;
@Component
public class MPADao implements MPAStorage {
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public MPADao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPARating getRatingById(int id) {
        try {
            String sqlReq = "SELECT rating_id, rating_name FROM mpa_rating WHERE rating_id = ?";
            return jdbcTemplate.queryForObject(sqlReq, new MPAMapper(), id);
        } catch (Exception e) {
            throw new NotFoundException("not found");
        }
    }

    @Override
    public Collection<MPARating> getAll() {
        String sqlReq = "SELECT rating_id, rating_name FROM mpa_rating";
        return jdbcTemplate.query(sqlReq, new MPAMapper());
    }
}
