package ru.practicum.model.compilation.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.event.Event;

import java.util.Set;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewCompilationMapper {

    @Mapping(source = "eventsSet", target = "events")
    Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto, Set<Event> eventsSet);
}
