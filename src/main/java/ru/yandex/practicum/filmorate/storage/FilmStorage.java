package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface FilmStorage {
    Collection<Film> getAll();
    Optional<Film> addFilm (Film film) throws ValidationException;

    Optional<Film> findFilmById (int id) throws NotFoundException;

    void deleteFilm (Film film);

    Optional<Film> updateFilm(Film film) throws NotFoundException, ValidationException;

    void deleteLike(int id, int userId);

    void likeFilm(int id, int userId);

    List<Film> mostLikedFilms(int count);
}
