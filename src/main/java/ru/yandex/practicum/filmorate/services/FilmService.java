package ru.yandex.practicum.filmorate.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public void likeFilm (int filmId, int userId){
        IdValidator(filmId,userId);
        for (Film film : inMemoryFilmStorage.getAll()){
            if (film.getId() == filmId){
                film.getLikes().add(userId);
                log.info("Liked");
                break;
            }
        }
    }

    public void deleteLike (int filmId, int userId){
        IdValidator(filmId,userId);
        for (Film film : inMemoryFilmStorage.getAll()){
            if (film.getId() == filmId){
                film.getLikes().remove(userId);
                log.info("Like deleted");
                break;
            }
        }
    }

    public List<Film> mostLikedFilms (int count){
        ArrayList <Film> popular = new ArrayList<>(inMemoryFilmStorage.getAll());
        return   popular.stream()
                .sorted((f1, f2)-> compare(f1,f2))
                .limit(count)
                .collect(Collectors.toList());
    }

    private int compare (Film film1, Film film2){
        return (film1.getLikes().size() - film2.getLikes().size())*-1; //обратный метод сортировки
    }

    private static void IdValidator (int filmId, int userId){
        if (filmId < 0 || userId < 0){
            throw new NotFoundException("Negative id");
        }
    }
}
