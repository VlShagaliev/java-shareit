package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemDto addItem(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.toItemDto(itemService.addItem(item, userId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItemById(@RequestBody Item item, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return ItemMapper.toItemDto(itemService.updateItemByIdItem(item, itemId, userId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable Long itemId) {
        return ItemMapper.toItemDto(itemService.getItemByItemId(itemId));
    }

    @GetMapping
    public Collection<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUserId(userId).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toSet());
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearchName(@RequestParam(value = "text", required = false) String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return itemService.getItemsBySearchName(text).stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toSet());
    }
}
