package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class FilmController {
    private Map<Integer, Film> films =new HashMap();

    private static int iD = 1;


    @GetMapping("/films")
    public List<Film> findFilms(){
        List<Film> filmList = new ArrayList<>(films.values());
        return filmList;
    }

    @PostMapping(value = "/films")
    public Film createFilm (@RequestBody Film film) throws ValidationException {
        if (validation(film)){
            film.setId(iD);
            films.put(iD, film);
            iD++;
            log.info("Запрос : POST, Фильм добавлен, Фильмов : '{}'", films.size());
            return film;
        } else {
            log.info("Неверно заполнены поля, валидация не пройдена");
            throw new ValidationException("Bad request!!!");
        }
    }

    @PutMapping(value = "/films")
    public Film updateFilm(@RequestBody Film film) throws ValidationException {
        if (films.containsKey(film.getId())){
            films.put(film.getId(), film);
            log.info("Запрос : PUT, Фильм обновлен");
        } else {
            log.info("Такого фильма не существует! Обновление не удалось!");
            throw new ValidationException("Ошибка обновления, фильм не найден!");
        }
        return film;
    }

    private static boolean validation (Film film) {
        LocalDate filmsBirthday = LocalDate.parse("1895-12-28");

        boolean valid = !film.getName().isBlank() && film.getDescription().length() <= 200 &&
                film.getDuration() > 0 && film.getReleaseDate().isAfter(filmsBirthday);

        return valid;
    }
}
