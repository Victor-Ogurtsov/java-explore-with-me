package ru.practicum.model.event.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserShortDto;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface EventFullMapper {
    @Mapping(source = "event.locationLat", target = "location.lat")
    @Mapping(source = "event.locationLon", target = "location.lon")
    @Mapping(target = "eventDate", source = "event.eventDate",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "createdOn", source = "event.createdOn",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    @Mapping(target = "publishedOn", source = "event.publishedOn",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventFullDto toEventFullDto(Event event, Integer views, Integer confirmedRequests);

    UserShortDto toUserShortDto(User user);

    CategoryDto toCategoryDto(Category category);
}