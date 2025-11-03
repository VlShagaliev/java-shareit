package ru.practicum.shareit.item.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.CommentMapper;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto createItem(ItemDto itemDto, Long userId) {
        User owner = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Item item = new Item();
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        item.setOwner(owner);

        Item savedItem = itemRepository.save(item);
        return ItemMapper.toItemDto(savedItem);
    }

    @Override
    @Transactional
    public ItemDto updateItemByIdItem(ItemDto itemDto, Long itemId, Long userId) {
        Item item = itemRepository.getReferenceById(itemId);
        item.setOwner(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден")));
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        Item updateItem = itemRepository.save(item);
        return ItemMapper.toItemDto(updateItem);
    }

    @Override
    public ItemDto getItemByItemId(Long itemId) {
        ItemDto itemDto = ItemMapper.toItemDto(itemRepository.getReferenceById(itemId));
        itemDto.setComments(commentRepository.findCommentsByItemId(itemId).stream()
                .map(CommentMapper::toCommentDto)
                .toList());
        return itemDto;
    }

    @Override
    public Collection<ItemDto> getItemsByUserId(Long userId) {
        List<Item> itemList = itemRepository.findAllByOwnerId(userId);
        return itemList.stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<ItemDto> getItemsBySearchName(String text) {
        List<Item> itemList = itemRepository.search(text);
        return itemList.stream()
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toSet());
    }
}