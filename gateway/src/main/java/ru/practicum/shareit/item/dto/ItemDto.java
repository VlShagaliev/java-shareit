package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {
    @NotBlank(message = "Имя предмета должно быть заполнено.")
    private String name;

    @NotBlank(message = "Описание предмета должно быть заполнено.")
    private String description;

    @NotNull(message = "Доступность предмета должна быть заполнена.")
    private Boolean available;

    private Long owner;
    private Long requestId;
}
