package ru.bank.bank_rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
@Schema(description = "ДТО кастомного исключения")
public class ExceptionDto {
    @Schema(description = "Сообщение об ошибке")
    private String message;

    @Schema(description = "Время ошибки")
    private LocalDateTime timestamp;
}
