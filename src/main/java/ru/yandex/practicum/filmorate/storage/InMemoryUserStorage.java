package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    private long ids = 0;
    private final Map<Long, User> users = new HashMap<>();

    private Long makeNewId() {
        ids = ids + 1;
        return ids;
    }

    @Override
    public User add(User user) {
        if (!users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getLogin());
                user.setId(makeNewId());
                users.put(user.getId(), user);
                log.debug("User added with name=login id={}", user.getId());
            } else {
                user.setId(makeNewId());
                users.put(user.getId(), user);
                log.debug("User added with id={}", user.getId());
            }
        } else {
            throw new ValidationException("This user already exists");
        }
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.debug("User added or updated with name=login id={}", user.getId());
        }
        users.put(user.getId(), user);
        log.debug("User added or updated id={}", user.getId());
        return user;
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<User>(users.values());
    }

    @Override
    public User findUserById(Long id) {
        checkContains(id);
        return users.get(id);
    }

    @Override
    public void checkContains(Long id) {
        if (!users.containsKey(id)) {
            throw new NotFoundException("User with id=" + id + "not found.");
        }
    }
}
