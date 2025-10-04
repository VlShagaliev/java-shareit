package ru.practicum.shareit.item.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
public class Item {
    private Long id;

    @NotBlank(message = "Наименование предмета не может быть пустым")
    private String name;

    @NotBlank(message = "Описание предмета не может быть пустым")
    private String description;

    @NotNull
    private Boolean available;

    private User owner;
    private ItemRequest request;
}
