package ru.bank.bank_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bank.bank_rest.dto.CardDto;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.requests.TransferMoneyRequest;
import ru.bank.bank_rest.service.CardService;
import ru.bank.bank_rest.service.UserService;

@RequestMapping("/api/card")
@RestController
@PreAuthorize("hasAuthority('USER')")
public class CardController {
    private final CardService cardService;
    private final UserService userService;

    @Autowired
    public CardController(CardService cardService, UserService userService) {
        this.cardService = cardService;
        this.userService = userService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "404", description = "Карты не существует")
    })
    @Operation(summary = "Получить банковскую карту")
    @GetMapping("/getCard/{id}")
    public ResponseEntity<CardDto> getCard(@PathVariable Long id) {
        return ResponseEntity.ok(cardService.getCard(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации")
    })
    @Operation(summary = "Получить банковске карты пользователя")
    @GetMapping("/getUserCards")
    public ResponseEntity<Page<CardDto>> getUserCards(@PageableDefault Pageable pageable) {
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(cardService.getUserCards(user, pageable));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "404", description = "Карты не существует")
    })
    @Operation(summary = "Узнать баланс на карте")
    @GetMapping("/getBalance/{id}")
    public ResponseEntity<Long> getBalance(@PathVariable Long id) {
        User user = userService.getCurrentUser();

        return ResponseEntity.ok(cardService.getBalance(id, user));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "404", description = "Карты не существует")
    })
    @Operation(summary = "Перевести деньги")
    @PutMapping("/transferMoney")
    public void transferMoney(@RequestBody @Valid TransferMoneyRequest request) {
        User user = userService.getCurrentUser();

        cardService.transferMoney(request, user);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "400", description = "Карта уже заблокирована")
    })
    @Operation(summary = "Отправить запрос на блокировку карты")
    @PutMapping("/blockCardRequest/{id}")
    public void blockCardRequest(@PathVariable Long id) {
        cardService.blockCardRequest(id);
    }
}
