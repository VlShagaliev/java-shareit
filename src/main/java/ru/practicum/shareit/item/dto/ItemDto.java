package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.model.User;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
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
