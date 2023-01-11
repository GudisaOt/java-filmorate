package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Collection<Film> getAll();
    Film addFilm (Film film) throws ValidationException;

    Film findFilmById (int id) throws NotFoundException;

    void deleteFilm (Film film);

    Film updateFilm(Film film) throws NotFoundException;
}
