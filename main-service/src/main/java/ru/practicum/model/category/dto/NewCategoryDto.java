package ru.practicum.model.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewCategoryDto {
    @NotBlank
    @Size(min = 1, max = 50)
    private String name;
}
