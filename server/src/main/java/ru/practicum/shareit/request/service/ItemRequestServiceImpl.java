package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getItemRequestByUserId(Long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository.findAll().stream()
                .filter(itemRequest -> itemRequest.getRequestor().getId().equals(userId))
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();
        return itemRequestDtoList;
    }

    @Override
    public ItemRequestDto getItemRequestByItemRequestId(Long itemRequestId) {
        ItemRequestDto itemRequestDto = itemRequestRepository.findById(itemRequestId)
                .map(ItemRequestMapper::toItemRequestDto)
                .orElseThrow(() -> new NotFoundException("Запрос не найден"));
        List<ItemForItemRequestDto> itemList = itemRepository.findAllByRequest_Id(itemRequestId)
                .stream()
                .map(ItemMapper::toItemForRequestDto)
                .toList();
        itemRequestDto.setItems(itemList);
        return itemRequestDto;
    }
}
