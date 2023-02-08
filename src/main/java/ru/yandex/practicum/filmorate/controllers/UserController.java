package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@RestController
@Slf4j

public class UserController {
    @Autowired
    @Qualifier("userDao")
    private final UserStorage userStorage;

    public UserController(@Qualifier("userDao") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @GetMapping("/users")
    public Collection<User> allUsers(){
        return userStorage.getAll();
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById (@PathVariable int id){
        return userStorage.getUserById(id);
    }

    @PostMapping(value = "/users")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "birthday")
    public User createUser (@RequestBody User user) throws ValidationException {
        return userStorage.addUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        return userStorage.updateUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend (@PathVariable int id, @PathVariable int friendId){
        userStorage.addFriend(id,friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable int id, @PathVariable int friendId){
        userStorage.deleteFriend(id,friendId);
    }

    @DeleteMapping(value = "/users")
    public void deleteUser (@RequestBody User user){
        userStorage.deleteUser(user);
    }

    @GetMapping(value = "/users/{id}/friends")
    public Collection<User> getAllFriends (@PathVariable int id){
        return userStorage.getAllFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public Collection<User> getMutualFriend (@PathVariable int id, @PathVariable int otherId){
        return userStorage.mutualFriends(id,otherId);
    }
}
