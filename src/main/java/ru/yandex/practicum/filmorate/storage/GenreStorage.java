package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;

public interface GenreStorage {
    Genre getById (int id);
    Collection<Genre> getAll();
    List<Genre> getFilmGenres (int id);
}
