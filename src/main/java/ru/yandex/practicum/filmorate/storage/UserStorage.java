package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    Collection<User> getAll();

    User getUserById (int id);

    User addUser (User user) throws ValidationException;

    void deleteUser (User user);

    User updateUser (User user) throws ValidationException;
}
