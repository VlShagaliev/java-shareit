package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorageImpl;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemStorageImpl itemStorage;
    private final UserService userService;

    public Item addItem(ItemDto item, Long userId) {
        item.setId(itemStorage.getItemStorage().size() + 1L);
        item.setOwner(userService.getUser(userId));
        return itemStorage.add(ItemMapper.toItem(item));
    }

    public Item updateItemByIdItem(ItemDto item, Long itemId, Long userId) {
        itemStorageHaveId(itemId);
        item.setId(itemId);
        item.setOwner(userService.getUser(userId));
        if (item.getName() == null) {
            item.setName(itemStorage.getItemStorage().get(itemId).getName());
        }
        if (item.getAvailable() == null) {
            item.setAvailable(itemStorage.getItemStorage().get(itemId).getAvailable());
        }
        if (item.getDescription() == null) {
            item.setDescription(itemStorage.getItemStorage().get(itemId).getDescription());
        }
        return itemStorage.update(ItemMapper.toItem(item));
    }

    public Item getItemByItemId(Long itemId) {
        itemStorageHaveId(itemId);
        return itemStorage.getItemStorage().get(itemId);
    }

    public Collection<Item> getItemsByUserId(Long userId) {
        return itemStorage.getItemStorage().values().stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toSet());
    }

    public Collection<Item> getItemsBySearchName(String text) {
        return itemStorage.getItemStorage().values().stream()
                .filter(item -> item.getName().toLowerCase().contains(text.toLowerCase()) && item.getAvailable())
                .collect(Collectors.toSet());
    }

    private void itemStorageHaveId(Long itemId) {
        if (!itemStorage.getItemStorage().containsKey(itemId)) {
            throw new NotFoundException(String.format("Предмета с id = %d не существует", itemId));
        }
    }
}
