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
    Long id;
    List<EventShortDto> events;
    Boolean pinned = false;
    String title;
}
