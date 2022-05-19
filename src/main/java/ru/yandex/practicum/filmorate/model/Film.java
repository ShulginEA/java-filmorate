package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    private final long id;
    @NotBlank
    private final String name;
    @Size(max = 200)
    @NotBlank
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
