package ru.practicum.model.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CompilationMapper {

    @Mapping(source = "eventShortDtoList", target = "events")
    CompilationDto toCompilationDto(Compilation compilation, List<EventShortDto> eventShortDtoList);
}
/*
public class Compilation {

    Long id;
    Set<Event> events = new HashSet<Event>();
    Boolean pinned = false;
    String title;
}

public class CompilationDto {
    Long id;
    List<EventShortDto> events;
    Boolean pinned = false;
    String title;
}
 */
