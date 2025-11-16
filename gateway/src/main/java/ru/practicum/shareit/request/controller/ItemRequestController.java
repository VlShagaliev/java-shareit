package ru.practicum.shareit.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.ItemRequestClient;
import ru.practicum.shareit.request.dto.ItemRequestDto;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemRequestController {

    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                                    @RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на создание запроса предмета");
        return itemRequestClient.createItemRequest(itemRequestDto, userId);
    }

    @GetMapping
    ResponseEntity<Object> getItemRequestByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка запросов предметов пользователя id: {}", userId);
        return itemRequestClient.getItemRequestByUserId(userId);
    }

    @GetMapping("/all")
    ResponseEntity<Object> getItemRequestsByOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        log.info("Запрос на получение списка запросов предметов других пользователей");
        return itemRequestClient.getItemRequestsByOtherUsers(userId);
    }

    @GetMapping("/{requestId}")
    ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long userId,
                                              @PathVariable Long requestId) {
        log.info("Запрос на получение запроса предмета по id: {}", requestId);
        return itemRequestClient.getItemRequestById(requestId);
    }
}
