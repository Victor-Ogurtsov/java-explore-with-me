package ru.practicum.model.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.model.user.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface NewUserRequestMapper {

    User fromNewUserRequest(NewUserRequest newUserRequest);
}
