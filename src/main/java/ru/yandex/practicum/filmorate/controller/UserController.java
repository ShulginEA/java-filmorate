package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public List<User> getAll() {
        return new ArrayList<User>(users.values());
    }

    @PostMapping
    public User add(@Valid @NotNull @RequestBody User user) {
        if (!users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                users.put(user.getId(), user);
                log.debug("User added with name=login id={}",user.getId());
            } else {
                users.put(user.getId(), user);
                log.debug("User added with id={}",user.getId());
            }
        } else {
            throw new ValidationException("This user already exists");
        }
        return user;
    }

    @PutMapping
    public User update(@Valid @NotNull @RequestBody User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User added or updated with name=login id={}",user.getId());
        }
        users.put(user.getId(), user);
        log.debug("User added or updated id={}",user.getId());
        return user;
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> exceptionHandler(ValidationException e) {
        log.debug(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
