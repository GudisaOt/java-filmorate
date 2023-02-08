package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Data

public class Film {
    private int id;
    private  String name;
    private  String description;
    private   List<Genre> genres;
    private  MPARating mpa;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "releaseDate")
    private  LocalDate releaseDate;
    @Builder
    public Film(int id, String name, String description, List<Genre> genres, MPARating mpa, LocalDate releaseDate, int duration) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.genres = genres;
        this.mpa = mpa;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
    private  int duration;
    private Set<Integer> likes;
}
