package ru.bank.bank_rest.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.bank.bank_rest.dto.CardDto;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.requests.CreateCardRequest;
import ru.bank.bank_rest.service.AdminService;

import java.util.List;

@RequestMapping("/api/admin")
@RestController
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации")
    })
    @Operation(summary = "Получить все карты в системе")
    @GetMapping("/getAllCards")
    public ResponseEntity<List<CardDto>> getAllCards() {
        return ResponseEntity.ok(adminService.getAllCards());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @Operation(summary = "Создать банковскую карту")
    @PostMapping("/createCard")
    public ResponseEntity<Card> createCard(@RequestBody @Valid CreateCardRequest request) {
        return ResponseEntity.ok(adminService.createCard(request));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "400", description = "Карта уже заблокирована")
    })
    @Operation(summary = "Заблокировать банковскую карту")
    @PutMapping("/blockCard/{id}")
    public ResponseEntity<Card> blockCard(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.blockCard(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "400", description = "Карта уже активирована")
    })
    @Operation(summary = "Активировать банковскую карту")
    @PutMapping("/activateCard/{id}")
    public ResponseEntity<Card> activateCard(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.activateCard(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "404", description = "Карты не существует")
    })
    @Operation(summary = "Удалить банковскую карту")
    @DeleteMapping ("/deleteCard/{id}")
    public void deleteCard(@PathVariable Long id) {
        adminService.deleteCard(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @Operation(summary = "Удалить пользователя")
    @DeleteMapping ("/deleteUser/{id}")
    public void deleteUser(@PathVariable Long id) {
        adminService.deleteUser(id);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "403", description = "Ошибка авторизации"),
            @ApiResponse(responseCode = "400", description = "Пользователь уже Админ")
    })
    @Operation(summary = "Выдать пользователю админку")
    @PutMapping("/giveAdminRights/{id}")
    public void giveAdminRights(@PathVariable Long id) {
        adminService.giveAdminRights(id);
    }
}
