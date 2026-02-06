package ru.bank.bank_rest.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
@Schema(description = "Запрос на перевод между своими картами")
public class TransferMoneyRequest {
    @Schema(description = "Карта, с которой отправляются деньги")
    @NotBlank(message = "Номер карты не может быть null")
    @Pattern(regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$", message = "Номер карты должен соответствовать формату '2222 1111 2222 1111'")
    private String numberFrom;

    @Schema(description = "Карта, куда деньги поступают")
    @NotBlank(message = "Номер карты не может быть null")
    @Pattern(regexp = "^\\d{4} \\d{4} \\d{4} \\d{4}$", message = "Номер карты должен соответствовать формату '2222 1111 2222 1111'")
    private String numberTo;

    @Schema(description = "Сумма перевода")
    @NotNull(message = "Баланс не может быть null")
    @PositiveOrZero(message = "Баланс должен быть неотрицательным")
    private Long balance;
}
