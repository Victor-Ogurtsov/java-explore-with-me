package ru.practicum.model.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.model.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserShortMapper {

    UserShortDto toUserShortDto(User user);
}
