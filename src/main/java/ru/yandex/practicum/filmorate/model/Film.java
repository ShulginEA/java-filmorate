package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

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
    @Length(max = 200)
    @NotBlank
    private final String description;
    private final LocalDate releaseDate;
    @Positive
    private final int duration;
}
