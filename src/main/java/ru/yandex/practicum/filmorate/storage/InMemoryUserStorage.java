package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private static int iD = 1;
    private HashMap<Integer, User> users =new HashMap<>();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getUserById(int id) {
        if (users.containsKey(id)){
            log.info("GET запрос, пользователь передан");
            return users.get(id);
        } else {
            throw new NotFoundException("Not found");
        }
    }

    @Override
    public User addUser(User user) throws ValidationException {
        boolean isNameEmpty = user.getName() == null || user.getName().isBlank();
        if (isNameEmpty) {
            user.setName(user.getLogin());
        }
        validation(user);
        user.setId(iD);
        users.put(iD, user);
        iD++;
        log.info("Запрос : POST,  Пользователь добавлен, Пользователей : '{}'", users.size());
        return user;
    }

    @Override
    public User updateUser(User user) throws NotFoundException {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
            log.info("Запрос : PUT, Пользователь обновлен");
        } else {
            log.info("Такого пользователя не существует! Обновление не удалось!");
            throw new NotFoundException("Ошибка обновления, пользователь не найден!");
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (users.containsKey(user.getId())){
            users.remove(user.getId());
            log.info("Пользоователь '{}' удален", user.getLogin());
        } else {
            log.info("User not found!!!");
        }
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
