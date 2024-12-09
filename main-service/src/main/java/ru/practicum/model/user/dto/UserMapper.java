package ru.practicum.model.user.dto;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import ru.practicum.model.user.User;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {
    UserDto toUserDto(User user);

    User fromUserDto(UserDto userDto);

    List<UserDto> toUserDtoList(List<User> userList);
}
