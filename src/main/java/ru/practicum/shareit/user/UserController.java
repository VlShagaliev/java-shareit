package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserDto newUser(@Valid @RequestBody User user) throws ValidationException {
        return UserMapper.toUserDto(userService.addUser(user));
    }

    @PatchMapping("/{id}")
    public UserDto updateUser(@RequestBody User user, @PathVariable Long id) throws ValidationException {
        return UserMapper.toUserDto(userService.updateUser(user, id));
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return UserMapper.toUserDto(userService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
