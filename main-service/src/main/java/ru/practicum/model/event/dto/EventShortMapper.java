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
public interface EventShortMapper {

    @Mapping(target = "eventDate", source = "event.eventDate",
            dateFormat = "yyyy-MM-dd HH:mm:ss")
    EventShortDto toEventShort(Event event, Integer views, Integer confirmedRequests);

    UserShortDto toUserShortDto(User user);

    CategoryDto toCategoryDto(Category category);
}