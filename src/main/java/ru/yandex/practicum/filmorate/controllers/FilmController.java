package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@RestController
@Slf4j
public class FilmController {
    @Qualifier("filmDao")
    private final FilmStorage filmStorage;

    public FilmController(@Qualifier("filmDao") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    @GetMapping("/films")
    public Collection<Film> findFilms(){
        return filmStorage.getAll();
    }

    @GetMapping(value = "/films/{id}")
    public Optional<Film> findFilmById (@PathVariable int id){
        return filmStorage.findFilmById(id);
    }

    @PostMapping(value = "/films")
    public Optional<Film> createFilm (@RequestBody Film film) throws ValidationException {
        return filmStorage.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Optional<Film> updateFilm(@RequestBody Film film) throws ValidationException {
        return filmStorage.updateFilm(film);
    }

    @PutMapping (value = "/films/{id}/like/{userId}")
    public void likeFilm (@PathVariable int id, @PathVariable int userId){
        filmStorage.likeFilm(id, userId);
    }

    @DeleteMapping (value = "/films/{id}/like/{userId}")
    public void deleteLike (@PathVariable int id, @PathVariable int userId){
        filmStorage.deleteLike(id, userId);
    }

    @GetMapping (value = "/films/popular")
    public List<Film> getMostPopular (@RequestParam (defaultValue = "10") int count){
        return filmStorage.mostLikedFilms(count);
    }
}
