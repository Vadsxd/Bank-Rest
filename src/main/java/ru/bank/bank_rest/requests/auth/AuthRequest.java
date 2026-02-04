package ru.bank.bank_rest.requests.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class AuthRequest {
    @Schema(description = "Логин пользователя", example = "ExampleLogin")
    @Size(min = 5, max = 50, message = "Логин пользователя должен содержать от 5 до 50 символов")
    @NotBlank(message = "Логин пользователя не может быть пустыми")
    private String login;

    @Schema(description = "Пароль", example = "example_password")
    @Size(min = 5, max = 255, message = "Длина пароля должна быть от 5 до 255 символов")
    @NotBlank(message = "Пароль не может быть пустыми")
    private String password;
}
