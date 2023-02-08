package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
@Builder
public class User {
    private int id;
    private  String email;
    private  String login;
    private String name;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private  LocalDate birthday;
    private Set<Integer> friends;
    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        if (this.name.isBlank()){
            return this.login;
        }
        return name;
    }
    public void addFriend(int friendId){
        friends.add(friendId);
    }
}
