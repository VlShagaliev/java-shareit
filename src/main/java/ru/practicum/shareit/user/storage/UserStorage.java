package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {
    User add(User user);

    User update(User user);

    Collection<User> users();

    User get(Long id);

    void delete(Long id);
}
