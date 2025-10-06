package ru.practicum.shareit.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class User {
    private Long id;

    @NotBlank(message = "Логин/имя не может быть пустым")
    private String name;

    @Email
    @NotBlank(message = "Почта не может быть пустой")
    private String email;
}
