package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPARating;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.Collection;

@RestController
@Slf4j
public class MPAController {
    private final MPAStorage mpaStorage;

    public MPAController(MPAStorage mpaStorage) {
        this.mpaStorage = mpaStorage;
    }

    @GetMapping("/mpa")
    public Collection<MPARating> getAll(){
        return mpaStorage.getAll();
    }

    @GetMapping("/mpa/{id}")
    public MPARating findById (@PathVariable int id) {
        return mpaStorage.getRatingById(id);
    }
}
