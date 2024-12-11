package ru.practicum.model.complaint.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class NewComplaintComment {
    @NotBlank
    @Size(min = 1, max = 2000)
    private String description;
}
