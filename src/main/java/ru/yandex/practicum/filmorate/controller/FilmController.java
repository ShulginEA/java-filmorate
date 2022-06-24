package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class FilmController {

    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/films")
    public List<Film> allFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/films/{id}")
    public Film findFilmById(@PathVariable Long id) {
        checkFilmId(id);
        return filmService.getFilmById(id);
    }

    @GetMapping("/films/popular")
    public List<Film> bestFilmsByLike(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.bestFilmsByLike(count);
    }

    @PostMapping("/films")
    public Film addFilm(@Valid @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable Long id, @PathVariable Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmService.addLike(userId, id);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable Long id, @PathVariable Long userId) {
        checkFilmId(id);
        checkUserId(userId);
        filmService.deleteLike(id, userId);
    }

    private void checkFilmId(Long id) {
        if (id == null) {
            throw new ValidationException("wrong film id");
        }
    }

    private void checkUserId(Long userId) {
        if (userId == null) {
            throw new ValidationException("wrong user id");
        }
    }
}
