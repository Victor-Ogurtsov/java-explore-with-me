package ru.practicum.model.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewComment {
    @NotBlank
    @Size(min = 1, max = 7000)
    private String text;
}
