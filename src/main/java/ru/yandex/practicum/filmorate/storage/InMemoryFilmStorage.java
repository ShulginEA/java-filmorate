package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private long ids = 0;
    private final Map<Long, Film> films = new HashMap<>();

    private Long makeNewId() {
        ids = ids + 1;
        return ids;
    }

    @Override
    public Film add(Film film) {
        if (!films.containsKey(film.getId())) {
            film.setId(makeNewId());
            films.put(film.getId(), film);
            log.debug("Film added id={}", film.getId());
        } else {
            throw new ValidationException("This movie already exists");
        }
        return film;
    }

    @Override
    public Film update(Film film) {
        films.put(film.getId(), film);
        log.debug("Film added or update id={}", film.getId());
        return film;
    }

    @Override
    public List<Film> getAll() {
        return new ArrayList<Film>(films.values());
    }

    @Override
    public Film findFilmById(Long id) {
        checkContains(id);
        return films.get(id);
    }

    @Override
    public void checkContains(Long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Film with id=" + id + "not found.");
        }
    }

}
