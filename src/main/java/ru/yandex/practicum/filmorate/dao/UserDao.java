package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mappers.UserMapper;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.other.FriendStatus;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;

@Component
@Qualifier("userDao")
@RequiredArgsConstructor
public class UserDao implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final static String UNCONFIRMED = "UNCONFIRMED";
    private final static String CONFIRMED = "CONFIRMED";

    @Override
    public Collection<User> getAll() {
     String sqlRequest = "select * from users";

        return jdbcTemplate.query(sqlRequest, new UserMapper());
    }
    @Override
    public User getUserById(int id) {
        try {
            String findReq = "select * from users where user_id=?";
            return jdbcTemplate.queryForObject(findReq, new UserMapper(), id);
        }catch (Exception e) {
            throw new NotFoundException("user not found!");
            }
    }

    @Override
    public User addUser(User user) throws ValidationException {
        validation(user);
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.withTableName("users");
        simpleJdbcInsert.usingGeneratedKeyColumns("user_id");
            int genId = (int) simpleJdbcInsert.executeAndReturnKey(Map.of(
                    "name", user.getName(),
                    "login", user.getLogin(),
                    "email", user.getEmail(),
                    "birthday", user.getBirthday()
            ));
            user.setId(genId);

            return getUserById(user.getId());

    }

    @Override
    public void deleteUser(User user) {
        String delReq = "DELETE FROM users WHERE user_id = ?";
        jdbcTemplate.update(delReq, user.getId());
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        try {
            String updReq = "UPDATE users SET name = ?, email = ?, login = ?, birthday = ? WHERE user_id = ?";
            jdbcTemplate.update(updReq, user.getName(), user.getEmail(), user.getLogin(), user.getBirthday(), user.getId());
            return getUserById(user.getId());
        } catch (Exception e){
            throw new NotFoundException("id not found");
        }
    }

    public void addFriend (int userId, int friendId) {
        try {
            jdbcTemplate.update("INSERT INTO FRIENDS (user_id, friend_id, status) values ( ?,?,? )", userId, friendId,
                    UNCONFIRMED);
        }catch (Exception e){
            throw new NotFoundException("user not found!");
        }
    }

    public void deleteFriend (int userId, int friendId) {
        jdbcTemplate.update("DELETE FROM FRIENDS WHERE USER_ID = ? AND FRIEND_ID = ?", userId,friendId);
    }

    public Collection<User> getAllFriends (int userId) {
        String request = "SELECT " +
                "   users.user_id, " +
                "   users.email, " +
                "   users.name, " +
                "   users.login, " +
                "   users.birthday " +
                "FROM users " +
                "JOIN friends ON users.user_id = friends.friend_id " +
                "WHERE friends.user_id = ?";
        return jdbcTemplate.query(request, new UserMapper(), userId);
    }
    @Override
    public Collection<User> mutualFriends (int userId, int otherId) {
        String sqlRequest = "SELECT u.user_id," +
                "   u.name," +
                "   u.login," +
                "   u.email," +
                "   u.birthday " +
                "FROM users u " +
                "JOIN (" +
                "   SELECT  friend_id " +
                "   FROM FRIENDS " +
                "   WHERE user_id = ? " +
                "   INTERSECT " +
                "   SELECT friend_id " +
                "   FROM FRIENDS " +
                "   WHERE user_id = ? " +
                ") m_friends ON u.USER_ID = m_friends.FRIEND_ID;";
        return jdbcTemplate.query(sqlRequest, new UserMapper(), userId, otherId);
    }

    private static void validation (User user) throws ValidationException {
        boolean isMailCorrect = !user.getEmail().isEmpty() && user.getEmail().contains("@");
        boolean isLoginCorrect = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        boolean isBirthdayCorrect = user.getBirthday().isBefore(LocalDate.now());
        boolean valid = isBirthdayCorrect && isLoginCorrect && isMailCorrect;

        if (!valid) {
            throw new ValidationException("Bad request!!!");
        }
    }
}
