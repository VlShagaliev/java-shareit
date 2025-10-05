package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorageImpl;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorageImpl userStorage;

    public User addUser(UserDto user) throws ValidationException {
        userStorageHaveEmail(user);
        user.setId(userStorage.getUserStorage().size() + 1L);
        return userStorage.add(UserMapper.toUser(user));
    }

    public User updateUser(UserDto user, Long id) throws ValidationException {
        userStorageHaveEmail(user);
        user.setId(id);
        userStorageHaveId(id);
        return userStorage.update(UserMapper.toUser(user));
    }

    public User getUser(Long id) {
        userStorageHaveId(id);
        return userStorage.get(id);
    }

    public void deleteUser(Long id) {
        userStorageHaveId(id);
        userStorage.delete(id);
    }

    private void userStorageHaveEmail(UserDto user) throws ValidationException {
        if (userStorage.getUserStorage().values().stream()
                .anyMatch(user1 -> user1.getEmail().equals(user.getEmail()))) {
            throw new ValidationException("Пользователь с данной почтой уже существует");
        }
    }

    private void userStorageHaveId(Long id) {
        if (!userStorage.getUserStorage().containsKey(id)) {
            throw new NotFoundException(String.format("Пользователя с id = %d не существует", id));
        }
    }
}
