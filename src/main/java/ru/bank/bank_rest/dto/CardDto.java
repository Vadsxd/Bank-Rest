package ru.bank.bank_rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import ru.bank.bank_rest.entity.enums.CardStatus;

import java.time.LocalDate;

@Builder
@Data
@Schema(description = "Дто банковской карты")
public class CardDto {
    @Schema(description = "Логин пользователя карты")
    private String login;

    @Schema(description = "Номер карты (отображается маской)")
    private String number;

    @Schema(description = "Срок карты")
    private LocalDate expireTime;

    @Schema(description = "Баланс карты")
    private Long balance;

    @Schema(description = "Статус карты")
    private CardStatus cardStatus;

    @Schema(description = "Запрос на блокировку карты (true - активный)")
    private Boolean blockRequest;
}
