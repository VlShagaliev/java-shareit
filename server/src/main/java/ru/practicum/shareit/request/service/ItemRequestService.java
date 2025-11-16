package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getItemRequestByUserId(Long userId);

    ItemRequestDto getItemRequestByItemRequestId(Long itemRequestId);

    List<ItemRequestDto> getItemRequestsByOtherUsers(Long userId);
}
