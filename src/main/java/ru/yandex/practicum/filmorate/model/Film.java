package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.other.Genre;
import ru.yandex.practicum.filmorate.other.MPARating;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;


@Data
public class Film {
    private int id;
    private final String name;
    private final String description;
    private final Genre genre;
    private final String mPA;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "releaseDate")
    private final LocalDate releaseDate;
    private final int duration;
    private Set<Integer> likes;

    @Builder
    public Film(String name, String description, Genre genre, String mPA, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.genre = genre;
        this.mPA = mPA;
        this.releaseDate = releaseDate;
        this.duration = duration;
        likes = new HashSet<>();
    }
}

