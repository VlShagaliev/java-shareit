package ru.practicum.shareit.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDto {
    @NotBlank(message = "Имя должно быть указано.")
    private String name;

    @NotBlank(message = "Электронный адрес должен быть указан.")
    @Email(message = "Электронный адрес некорректен.")
    private String email;
}
