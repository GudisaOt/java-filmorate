package ru.yandex.practicum.filmorate.exceptions;

public class NotFoundException extends  RuntimeException{

    public NotFoundException (String m){
        super(m);
    }
}
