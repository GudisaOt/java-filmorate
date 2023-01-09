package ru.yandex.practicum.filmorate.model;

public class ErrorRespones {
    private String error;

    public ErrorRespones(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
