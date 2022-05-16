package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate DATE_OF_BIRTH_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);

    @GetMapping
    public List<Film> getAll() {
        return new ArrayList<Film>(films.values());
    }

    @PostMapping
    public Film add(@Valid @NotNull @RequestBody Film film) {
        if (!films.containsKey(film.getId())) {
            if (checkAFilm(film)) {
                films.put(film.getId(), film);
                log.debug("Film added id={}", film.getId());
            }
        } else {
            throw new ValidationException("This movie already exists");
        }
        return film;
    }

    @PutMapping
    public Film update(@Valid @NotNull @RequestBody Film film) {
        if (checkAFilm(film)) {
            films.put(film.getId(), film);
            log.debug("Film added or update id={}", film.getId());
        }
        return film;
    }

    private boolean checkAFilm(Film film) {
        if (film.getReleaseDate().isAfter(DATE_OF_BIRTH_CINEMA)) {
            return true;
        } else {
            throw new ValidationException("Release date must be after: " + DATE_OF_BIRTH_CINEMA);
        }
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> exceptionHandler(ValidationException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
