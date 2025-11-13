package ru.practicum.shareit.user.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
@RequiredArgsConstructor
@Getter
public class UserStorageImpl implements UserStorage {
    private final Map<Long, User> userStorage;
    private final Map<Long, String> userEmail;

    @Override
    public User add(User user) {
        userStorage.put(user.getId(), user);
        userEmail.put(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public User update(User user) {
        if (user.getName() == null) {
            user.setName(userStorage.get(user.getId()).getName());
        }
        if (user.getEmail() == null) {
            user.setEmail(userStorage.get(user.getId()).getEmail());
        }
        userStorage.put(user.getId(), user);
        userEmail.put(user.getId(), user.getEmail());
        return user;
    }

    @Override
    public Collection<User> users() {
        return List.of();
    }

    @Override
    public User get(Long id) {
        return userStorage.values().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .get();
    }

    @Override
    public void delete(Long id) {
        userStorage.remove(id);
        userEmail.remove(id);
    }
}
