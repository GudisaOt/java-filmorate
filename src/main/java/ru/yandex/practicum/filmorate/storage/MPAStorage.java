package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MPARating;

import java.util.Collection;

public interface MPAStorage {
    MPARating getRatingById (int id);
    Collection<MPARating> getAll();
}
