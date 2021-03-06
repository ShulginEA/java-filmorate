package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
class UserControllerTest {
    @Autowired
    private UserController userController;

    @Test
    void shouldAddUser() {
        User user = new User((long) 1, "aa@aaa.ru", "bbb", "ccc",
                LocalDate.of(2000, 1, 1));
        userController.addUser(user);
        Assertions.assertEquals(user, userController.allUsers().get(0));
    }

    @Test
    void shouldCantAddUserBecauseWrongDate() {
        User user = new User((long) 1, "aa@aaa.ru", "bbb", "ccc",
                LocalDate.of(3000, 1, 1));
        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user));
    }
}