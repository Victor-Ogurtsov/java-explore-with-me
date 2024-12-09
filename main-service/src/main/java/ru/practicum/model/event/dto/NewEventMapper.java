package ru.practicum.model.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.model.event.Event;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewEventMapper {

    @Mapping(source = "location.lat", target = "locationLat")
    @Mapping(source = "location.lon", target = "locationLon")
    @Mapping(target = "category", ignore = true)
    @Mapping(target = "eventDate", source = "eventDate",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    Event fromNewEventDto(NewEventDto newEventDto);
}