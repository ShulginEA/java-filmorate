package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users/{id}")
    public User findUserById(@PathVariable Long id) {
        checkUserId(id);
        return userService.findUserById(id);
    }

    @GetMapping("/users")
    public List<User> allUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> findCommonFriends(@PathVariable("id") Long userId, @PathVariable("otherId") Long id) {
        checkUserId(id);
        if (id == null) {
            throw new ValidationException(String.format("Не корректно введен id другого пользователя. otherId = %s", id));
        }
        return userService.getAllMutualFriendsById(userId, id);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> findAllFriends(@PathVariable("id") Long userId) {
        checkUserId(userId);
        return userService.getFriendsById(userId);
    }

    @PostMapping("/users")
    public @Valid User addUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @ResponseBody
    @PutMapping("/users")
    public User changeUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        checkUserId(userId);
        checkFriendId(friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void deleteFriends(@PathVariable("id") Long userId, @PathVariable("friendId") Long friendId) {
        checkUserId(userId);
        checkFriendId(friendId);
        userService.removeFriend(userId, friendId);
    }

    private void checkUserId(Long id) {
        if (id == null) {
            throw new ValidationException("wrong user id");
        }
    }

    private void checkFriendId(Long friendId) {
        if (friendId == null) {
            throw new ValidationException(("wrong friend id"));
        }
    }
}
