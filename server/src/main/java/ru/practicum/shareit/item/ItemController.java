package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.CommentService;
import ru.practicum.shareit.item.service.ItemService;

import java.util.Collection;
import java.util.Collections;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<ItemDto> addItem(@Valid @RequestBody ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto savedItem = itemService.createItem(itemDto, userId);
        return ResponseEntity.ok(savedItem);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<ItemDto> updateItemById(@RequestBody ItemDto itemDto, @PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemDto updatedItem = itemService.updateItemByIdItem(itemDto, itemId, userId);
        return ResponseEntity.ok(updatedItem);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<ItemDto> getItemById(@PathVariable Long itemId) {
        ItemDto itemDto = itemService.getItemByItemId(itemId);
        return ResponseEntity.ok(itemDto);
    }

    @GetMapping
    public Collection<ItemDto> getItemsByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.getItemsByUserId(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> getItemsBySearchName(@RequestParam(value = "text") String text) {
        if (text == null || text.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return itemService.getItemsBySearchName(text);
    }

    @PostMapping(("/{itemId}/comment"))
    public ResponseEntity<CommentDto> addComment(@PathVariable Long itemId, @RequestHeader("X-Sharer-User-Id") Long authorId, @RequestBody CommentDto commentDto) {
        CommentDto addComment = commentService.addComment(itemId, authorId, commentDto);
        return ResponseEntity.ok(addComment);
    }
}
