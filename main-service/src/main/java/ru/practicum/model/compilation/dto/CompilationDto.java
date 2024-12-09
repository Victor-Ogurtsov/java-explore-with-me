package ru.practicum.model.compilation.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.List;

@Setter
@Getter
@ToString
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned = false;
    private String title;
}
