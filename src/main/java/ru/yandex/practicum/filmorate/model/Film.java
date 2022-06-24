package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private Long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    @NotBlank
    private final String description;
    @NotNull
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
    private final Set<Long> likes = new HashSet<>();

    public void addLike(Long userId) {
        this.likes.add(userId);
    }

    public void removeLike(Long userId) {
        this.likes.remove(userId);
    }
}
