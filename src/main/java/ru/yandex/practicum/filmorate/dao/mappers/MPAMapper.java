package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.MPARating;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MPAMapper implements RowMapper<MPARating> {
    @Override
    public MPARating mapRow(ResultSet rs, int rowNum) throws SQLException {
        MPARating mpaRating = MPARating.builder()
                .id(rs.getInt("rating_id"))
                .name(rs.getString("rating_name"))
                .build();
        return mpaRating;
    }
}
