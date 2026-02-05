package ru.bank.bank_rest.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Запрос на создание карты")
public class CreateCardRequest {
    @Schema(description = "Id владельца карты", example = "32")
    @NotBlank(message = "Id не может быть пустым")
    private Long userId;

    @Schema(description = "Срок действия карты", example = "30.05.2024")
    @NotNull(message = "У карты не может отсутствовать срок действия")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate expireTime;

    @Schema(description = "Баланс", example = "1000")
    @NotNull(message = "Баланс не может быть null")
    private Long balance;
}
