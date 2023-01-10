package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.services.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final InMemoryFilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @GetMapping("/films")
    public Collection<Film> findFilms(){
        return inMemoryFilmStorage.getAll();
    }

    @GetMapping(value = "/films/{id}")
    public Film findFilmById (@PathVariable int id){
        return inMemoryFilmStorage.findFilmById(id);
    }

    @PostMapping(value = "/films")
    public Film createFilm (@RequestBody Film film) throws ValidationException {
        return inMemoryFilmStorage.addFilm(film);
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film){
        return inMemoryFilmStorage.updateFilm(film);
    }

    @PutMapping (value = "/films/{id}/like/{userId}")
    public void likeFilm (@PathVariable int id, @PathVariable int userId){
        filmService.likeFilm(id, userId);
    }

    @DeleteMapping (value = "/films/{id}/like/{userId}")
    public void deleteLike (@PathVariable int id, @PathVariable int userId){
        filmService.deleteLike(id, userId);
    }

    @GetMapping (value = "/films/popular")
    public List<Film> getMostPopular (@RequestParam (defaultValue = "10") int count){
        return filmService.mostLikedFilms(count);
    }
}
