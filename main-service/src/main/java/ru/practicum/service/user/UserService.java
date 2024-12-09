package ru.practicum.service.user;

import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto addUser(NewUserRequest newUserRequest);

    List<UserDto> getUserList(List<Long> ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
