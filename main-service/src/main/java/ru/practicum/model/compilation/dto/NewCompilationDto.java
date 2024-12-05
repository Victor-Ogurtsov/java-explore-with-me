package ru.practicum.model.compilation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@ToString
public class NewCompilationDto {
    List<Long> events = new ArrayList<>();
    Boolean pinned = false;
    @NotBlank
    @Size(min = 1, max = 50)
    String title;
}