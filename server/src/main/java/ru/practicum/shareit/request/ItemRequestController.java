package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;

/**
 * TODO Sprint add-item-requests.
 */
@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;

    @PostMapping
    public ResponseEntity<ItemRequestDto> createRequest(@RequestBody ItemRequestDto itemRequestDto,
                                                        @RequestHeader("X-Sharer-User-Id") Long userId) {
        ItemRequestDto itemRequestSaved = itemRequestService.createItemRequest(itemRequestDto, userId);
        return ResponseEntity.ok(itemRequestSaved);
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getRequestByUserId(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemRequestDto> itemRequestDtoList = itemRequestService.getItemRequestByUserId(userId);
        return ResponseEntity.ok(itemRequestDtoList);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getRequestByItemRequestIdAndUserId(@PathVariable("requestId") Long itemRequestId) {
        ItemRequestDto itemRequestDto = itemRequestService.getItemRequestByItemRequestId(itemRequestId);
        return ResponseEntity.ok(itemRequestDto);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getItemRequestsByOtherUsers(@RequestHeader("X-Sharer-User-Id") Long userId) {
        List<ItemRequestDto> itemRequestDtoList = itemRequestService.getItemRequestsByOtherUsers(userId);
        return ResponseEntity.ok(itemRequestDtoList);
    }
}
