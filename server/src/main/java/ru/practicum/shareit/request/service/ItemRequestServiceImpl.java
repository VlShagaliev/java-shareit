package ru.practicum.shareit.request.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemForItemRequestDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ItemRequestDto createItemRequest(ItemRequestDto itemRequestDto, Long userId) {
        User user = checkUserInDb(userId);
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemRequest.setRequestor(user);
        itemRequest.setCreated(LocalDateTime.now());
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(itemRequest));
    }

    @Override
    public List<ItemRequestDto> getItemRequestByUserId(Long userId) {
        checkUserInDb(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "created");
        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository.findAll(sort).stream()
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

    @Override
    public List<ItemRequestDto> getItemRequestsByOtherUsers(Long userId) {
        checkUserInDb(userId);

        List<ItemRequestDto> itemRequestDtoList = itemRequestRepository.findByRequestorIdNot(userId)
                .stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .toList();

        List<Item> itemList = itemRepository.findAllByRequest_IdNotNull();

        Map<Long, List<Item>> itemsByRequestId = itemList.stream()
                .filter(item -> item.getRequest() != null)
                .collect(Collectors.groupingBy(item -> item.getRequest().getId()));


        for (ItemRequestDto requestDto : itemRequestDtoList) {
            Long requestId = requestDto.getId();
            List<Item> itemsForRequest = itemsByRequestId.getOrDefault(requestId, Collections.emptyList());
            List<ItemForItemRequestDto> itemDtos = itemsForRequest.stream()
                    .map(ItemMapper::toItemForRequestDto)
                    .collect(Collectors.toList());
            requestDto.setItems(itemDtos);
        }
        return itemRequestDtoList;
    }

    private User checkUserInDb(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден."));
    }


}
