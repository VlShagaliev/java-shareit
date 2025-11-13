package ru.practicum.shareit.item.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Component
public class ItemMapper {
    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                item.getRequest() != null ? item.getRequest().getId() : null,
                null,
                null,
                null
        );
    }

    public static Item toItem(ItemDto dto, User owner, ItemRequest request) {
        Item item = new Item();
        item.setId(dto.getId());
        item.setName(dto.getName());
        item.setDescription(dto.getDescription());
        item.setAvailable(dto.getAvailable());
        item.setOwner(owner);
        item.setRequest(request);
        return item;
    }

    public static ItemForItemRequestDto toItemForRequestDto(Item item) {
        ItemForItemRequestDto itemForItemRequestDto = new ItemForItemRequestDto();
        itemForItemRequestDto.setId(item.getId());
        itemForItemRequestDto.setOwnerId(item.getOwner().getId());
        itemForItemRequestDto.setName(item.getName());
        return itemForItemRequestDto;
    }
}
