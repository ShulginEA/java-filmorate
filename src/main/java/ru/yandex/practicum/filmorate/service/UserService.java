package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getAllUsers() {
        return userStorage.getAll();
    }

    public User findUserById(Long id) {
        return userStorage.findUserById(id);
    }

    public User createUser(User user) {
        validateUser(user);
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        validateUser(user);
        userStorage.checkContains(user.getId());
        return userStorage.update(user);
    }

    public void addFriend(Long id, Long friendId) {
        userStorage.checkContains(id);
        userStorage.checkContains(friendId);
        userStorage.findUserById(id).addFriend(friendId);
        userStorage.findUserById(friendId).addFriend(id);
        log.debug("User with id {} and {} friended", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        userStorage.checkContains(id);
        userStorage.checkContains(friendId);
        if (!userStorage.findUserById(id).getFriends().contains(friendId)) {
            throw new NotFoundException(String.format("User with id (%s) " +
                    "not friend user with id (%s)", friendId, id));
        }
        if (!userStorage.findUserById(friendId).getFriends().contains(id)) {
            throw new NotFoundException(String.format("User with id (%s) " +
                    "not friend user with id (%s)", friendId, id));
        }
        userStorage.findUserById(id).removeFriend(friendId);
        userStorage.findUserById(friendId).removeFriend(id);
        log.debug("User with id {} and {} unfriended", id, friendId);
    }

    public List<User> getFriendsById(Long id) {
        userStorage.checkContains(id);
        return userStorage.findUserById(id).getFriends().stream()
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    public List<User> getAllMutualFriendsById(Long id, Long otherId) {
        userStorage.checkContains(id);
        userStorage.checkContains(otherId);
        Set<Long> friendsId = userStorage.findUserById(id).getFriends();
        Set<Long> friendsOtherId = userStorage.findUserById(otherId).getFriends();
        return friendsId.stream()
                .filter(friendsOtherId::contains)
                .map(userStorage::findUserById)
                .collect(Collectors.toList());
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException("Login include space");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Wrong birthday");
        }
        if (user.getName().isBlank()) {
            log.info("User name change to login");
            user.setName(user.getLogin());
        }
    }

}
