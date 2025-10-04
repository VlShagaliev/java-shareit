package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Collections;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/items")
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public Item addItem(@Valid @RequestBody Item item, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.addItem(item, userId);
    }

    @PatchMapping("/{itemId}")
    public Item updateItemById(@RequestBody Item item, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.updateItemByIdItem(item, itemId, userId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable Long itemId) {
        return itemService.getItemByItemId(itemId);
    }

    @GetMapping
    public Collection<Item> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<Item> getItemsBySearchName(@RequestParam(value = "text", required = false) String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return itemService.getItemsBySearchName(text);
    }
}
