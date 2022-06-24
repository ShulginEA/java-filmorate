package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    private static final LocalDate DATE_OF_BIRTH_CINEMA = LocalDate.of(1895, Month.DECEMBER, 28);

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(Long userId, Long filmId) {
        filmStorage.checkContains(filmId);
        userStorage.checkContains(userId);
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        film.addLike(user.getId());
    }

    public void deleteLike(Long filmId, Long userId) {
        filmStorage.checkContains(filmId);
        userStorage.checkContains(userId);
        Film film = filmStorage.findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        film.getLikes().remove(user.getId());
    }

    public List<Film> bestFilmsByLike(Integer count) {
        return filmStorage.getAll().stream()
                .sorted((o1, o2) -> {
                    int result = Integer.compare(o1.getLikes().size(), o2.getLikes().size());
                    return result * -1;
                }).limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAll();
    }

    public Film getFilmById(Long id) {
        return filmStorage.findFilmById(id);
    }

    public Film createFilm(Film film) {
        validateFilm(film);
        return filmStorage.add(film);
    }

    public Film updateFilm(Film film) {
        validateFilm(film);
        filmStorage.checkContains(film.getId());
        return filmStorage.update(film);
    }

    private void validateFilm(Film film) throws ValidationException {
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Description can be < 200");
        }
        if (film.getReleaseDate().isBefore(DATE_OF_BIRTH_CINEMA)) {
            throw new ValidationException("Release date can be after: " + DATE_OF_BIRTH_CINEMA);
        }
    }
}
