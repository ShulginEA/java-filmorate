package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    private final String textException;

    public ErrorResponse(String textException) {
        this.textException = textException;
    }
}
