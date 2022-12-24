package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class UserController {
    private static int iD = 1;
    private HashMap<Integer, User> users =new HashMap<>();

    @GetMapping("/users")
    public List<User> findUser(){
        List<User> userList = new ArrayList<>(users.values());
        return userList;
    }

    @PostMapping(value = "/users")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "birthday")
    public User createUser (@RequestBody User user) throws ValidationException {
        boolean isNameEmpty = user.getName() == null;
        if (isNameEmpty) {
            user.setName(user.getLogin());
        }
        if (validation(user)){
            user.setId(iD);
            users.put(iD, user);
            iD++;
            log.info("Запрос : POST,  Пользователь добавлен, Пользователей : '{}'", users.size());
            return user;
        } else {
            log.info("Неверно заполнены поля, валидация не пройдена");
            throw new ValidationException("Bad request!!!");
        }
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        if (users.containsKey(user.getId())){
            users.put(user.getId(), user);
            log.info("Запрос : PUT, Пользователь обновлен");
        } else {
            log.info("Такого пользователя не существует! Обновление не удалось!");
            throw new ValidationException("Ошибка обновления, пользователь не найден!");
        }
        return user;
    }

    private static boolean validation (User user){
        boolean isMailCorrect = !user.getEmail().isEmpty() && user.getEmail().contains("@");
        boolean isLoginCorrect = !user.getLogin().isBlank() && !user.getLogin().contains(" ");
        boolean isBirthdayCorrect = user.getBirthday().isBefore(LocalDate.now());
        boolean valid = isBirthdayCorrect && isLoginCorrect && isMailCorrect;

        return valid;

    }



}
