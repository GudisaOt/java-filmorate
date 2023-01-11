package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


@Data
public class User {
    private int id;
    private final String email;
    private final String login;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private final LocalDate birthday;
    private Set<Integer> friends;
    @Builder
    public User(String email, String login, LocalDate birthday, String name) {
        this.email = email;
        this.login = login;
        this.birthday = birthday;
        this.name = name;
        friends = new HashSet<>();
    }

    public void addFriend(int friendId){
        friends.add(friendId);
    }
}
