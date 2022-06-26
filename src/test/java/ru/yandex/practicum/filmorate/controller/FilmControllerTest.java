package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    private FilmController filmController;

    @Test
    void shouldAddFilm() {
        Film film = new Film((long) 1, "aaa", "bbb",
                LocalDate.of(2000, 1, 1), 120);
        filmController.addFilm(film);
        Assertions.assertEquals(film, filmController.allFilms().get(0));
    }

    @Test
    void shouldCantAddFilmBecauseWrongDate() {
        Film film = new Film((long) 1, "aaa", "bbb",
                LocalDate.of(1000, 1, 1), 120);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }

    @Test
    void shouldCantAddFilmBecauseDescriptionTooLongOrZero() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 200; i++) {
            sb.append("b");
        }
        String description = sb.toString();
        Film film = new Film((long) 1, "aaa", description,
                LocalDate.of(2000, 1, 1), 120);
        Assertions.assertThrows(ValidationException.class, () -> filmController.addFilm(film));
    }
}