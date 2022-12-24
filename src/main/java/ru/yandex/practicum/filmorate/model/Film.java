package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;


@Data
public class Film {
    private int id;
    private final String name;
    private final String description;
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JsonProperty(value = "releaseDate")
    private final LocalDate releaseDate;
    private final int duration;

    @Builder
    public Film(String name, String description, LocalDate releaseDate, int duration) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
    }
}
