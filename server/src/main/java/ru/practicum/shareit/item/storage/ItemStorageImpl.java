package ru.practicum.shareit.item.storage;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Getter
public class ItemStorageImpl implements ItemStorage {
    private final Map<Long, Item> itemStorage;

    @Override
    public Item add(Item item) {
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(Item item) {
        itemStorage.put(item.getId(), item);
        return item;
    }

    @Override
    public void remove(Long id) {
        itemStorage.remove(id);
    }
}
