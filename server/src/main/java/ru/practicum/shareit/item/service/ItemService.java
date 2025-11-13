package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.Collection;

public interface ItemService {
    ItemDto createItem(ItemDto itemDto, Long userId);

    ItemDto updateItemByIdItem(ItemDto itemDto, Long itemId, Long userId);

    ItemDto getItemByItemId(Long itemId);

    Collection<ItemDto> getItemsByUserId(Long userId);

    Collection<ItemDto> getItemsBySearchName(String text);
}