package ru.bank.bank_rest.exception.card;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bank.bank_rest.dto.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class CardExceptionHandler {
    @ExceptionHandler(value = CardNegativeBalanceException.class)
    public ResponseEntity<ExceptionDto> negativeBalance(CardNegativeBalanceException e) {
        return new ResponseEntity<>(
                ExceptionDto.builder()
                        .message("Wrong argument")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = CardOwnerException.class)
    public ResponseEntity<ExceptionDto> otherOwner(CardOwnerException e) {
        return new ResponseEntity<>(
                ExceptionDto.builder()
                        .message("Wrong request")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
}
