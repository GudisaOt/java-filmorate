package ru.yandex.practicum.filmorate.controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.services.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@RestController
@Slf4j
public class UserController {
    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping("/users")
    public Collection<User> allUsers(){
        return inMemoryUserStorage.getAll();
    }

    @GetMapping(value = "/users/{id}")
    public User getUserById (@PathVariable int id){
        return inMemoryUserStorage.getUserById(id);
    }

    @PostMapping(value = "/users")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @JsonProperty(value = "birthday")
    public User createUser (@RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.addUser(user);
    }

    @PutMapping(value = "/users")
    public User updateUser(@RequestBody User user) throws ValidationException {
        return inMemoryUserStorage.updateUser(user);
    }

    @PutMapping(value = "/users/{id}/friends/{friendId}")
    public void addFriend (@PathVariable int id, @PathVariable int friendId){
        userService.addFriend(id,friendId);
    }

    @DeleteMapping(value = "/users/{id}/friends/{friendId}")
    public void deleteFriend (@PathVariable int id, @PathVariable int friendId){
        userService.deleteFriend(id,friendId);
    }

    @GetMapping(value = "/users/{id}/friends")
    public Collection<User> getAllFriends (@PathVariable int id){
        return userService.getAllFriends(id);
    }

    @GetMapping(value = "/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriend (@PathVariable int id, @PathVariable int otherId){
        return userService.mutualFriends(id,otherId);
    }





}
