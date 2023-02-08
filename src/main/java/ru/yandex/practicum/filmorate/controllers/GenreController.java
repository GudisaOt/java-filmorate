package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;

@RestController
@Slf4j
public class GenreController {
    private final GenreStorage genreStorage;

    public GenreController(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }
    @GetMapping("/genres/{id}")
    public Genre findByID (@PathVariable int id) {
        return genreStorage.getById(id);
    }

    @GetMapping("/genres")
    public Collection<Genre> getAll () {
        return genreStorage.getAll();
    }
}
