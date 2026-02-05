package ru.bank.bank_rest.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.bank.bank_rest.dto.ExceptionDto;

import java.time.LocalDateTime;

@ControllerAdvice
public class IllegalArgumentExceptionHandler {
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> wrongNumberFormat(IllegalArgumentException e) {
        return new ResponseEntity<>(
                ExceptionDto.builder()
                        .message("Wrong argument")
                        .timestamp(LocalDateTime.now())
                        .build(),
                HttpStatus.BAD_REQUEST);
    }
}
