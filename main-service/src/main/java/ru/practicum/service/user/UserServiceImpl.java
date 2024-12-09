package ru.practicum.service.user;


import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.practicum.exception.ViolationRestrictException;
import ru.practicum.model.user.dto.NewUserRequest;
import ru.practicum.model.user.dto.NewUserRequestMapper;
import ru.practicum.repository.UserRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.user.QUser;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserDto;
import ru.practicum.model.user.dto.UserMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final NewUserRequestMapper newUserRequestMapper;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserDto addUser(NewUserRequest newUserRequest) {
        Optional<User> userOptional = userRepository.findByEmail(newUserRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new ViolationRestrictException("CONFLICT", "Integrity constraint has been violated.",
                    "could not execute statement; SQL [n/a]; constraint [uq_email]; nested exception" +
                            " is org.hibernate.exception.ConstraintViolationException: could not execute statement");
        }
        User user = newUserRequestMapper.fromNewUserRequest(newUserRequest);
        User addedUser = userRepository.save(user);
        return userMapper.toUserDto(addedUser);
    }

    @Override
    public List<UserDto> getUserList(List<Long> ids, Integer from, Integer size) {
        JPAQuery<User> jpaQuery = jpaQueryFactory.selectFrom(QUser.user).offset(from).limit(size);
        if (ids != null) {
            jpaQuery.where(QUser.user.id.in(ids));
        }
        List<User> userList = jpaQuery.fetch();
        return userMapper.toUserDtoList(userList);
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("NOT_FOUND", "The required object was not found.",
                "User with id=" + userId + " was not found"));
        userRepository.deleteById(userId);
    }
}
