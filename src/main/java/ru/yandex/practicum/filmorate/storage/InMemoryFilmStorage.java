package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private Map<Integer, Film> films =new HashMap();

    private static int iD = 1;

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film addFilm(Film film) throws ValidationException {
        validation(film);
        film.setId(iD);
        films.put(iD, film);
        iD++;
        log.info("Запрос : POST, Фильм добавлен, Фильмов : '{}'", films.size());
        return film;
    }

    @Override
    public Film findFilmById(int id) throws NotFoundException {
        if (films.containsKey(id)){
            log.info("GET запрос, поиск фильма по ID");
            return films.get(id);
        } else {
            throw new NotFoundException("Фильма с таким ID не существует");
        }
    }

    @Override
    public Film updateFilm(Film film) throws NotFoundException {
        if (films.containsKey(film.getId())){
            films.put(film.getId(), film);
            log.info("Запрос : PUT, Фильм обновлен");
        } else {
            log.info("Такого фильма не существует! Обновление не удалось!");
            throw new NotFoundException("Ошибка обновления, фильм не найден!");
        }
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        if (films.containsKey(film.getId())){
            films.remove(film.getId());
            log.info("Фильм '{}' удален!", film.getName());
        } else {
            log.info("Film not found");
        }
    }

    private static void validation (Film film) throws ValidationException {
        LocalDate filmsBirthday = LocalDate.parse("1895-12-28");

        boolean valid = !film.getName().isBlank() && film.getDescription().length() <= 200 &&
                film.getDuration() > 0 && film.getReleaseDate().isAfter(filmsBirthday);

        if (!valid) {
            throw new ValidationException("Bad request!!!");
        }
    }

}
