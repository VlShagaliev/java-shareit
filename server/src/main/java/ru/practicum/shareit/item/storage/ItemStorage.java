package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

public interface ItemStorage {
    Item add(Item item);

    Item update(Item item);

    void remove(Long id);
}
