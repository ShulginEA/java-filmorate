package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    @Past
    private final LocalDate birthday;
    private final Set<Long> friends = new HashSet<>();

    public void addFriend(Long friendId) {
        this.friends.add(friendId);
    }

    public void removeFriend(Long friendId) {
        this.friends.remove(friendId);
    }

}
