package ru.bank.bank_rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.entity.enums.CardStatus;
import ru.bank.bank_rest.entity.enums.Role;
import ru.bank.bank_rest.repository.CardRepo;
import ru.bank.bank_rest.repository.UserRepo;
import ru.bank.bank_rest.requests.auth.AuthRequest;
import ru.bank.bank_rest.service.AuthService;
import ru.bank.bank_rest.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class AdminControllerTest {
    private static final String TEST_LOGIN = "TestLogin";
    private static String TOKEN = "";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CardRepo cardRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @AfterEach
    public void resetDb() {
        cardRepo.deleteAll();
        userRepo.deleteAll();
    }

    private AuthRequest createTestAuthRequest() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setLogin(TEST_LOGIN);
        authRequest.setPassword("123");

        return authRequest;
    }

    private void authorization() {
        authService.signUp(createTestAuthRequest());

        createAdmin();

        TOKEN = authService.signIn(createTestAuthRequest()).getToken();
    }

    private void createAdmin() {
        User user = userService.getByLogin(TEST_LOGIN);

        user.setRole(Role.ADMIN);
        userRepo.save(user);
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setUser(userService.getByLogin(TEST_LOGIN));
        card.setCardStatus(CardStatus.ACTIVE);
        card.setBalance(1000L);
        card.setBlockRequest(false);
        card.setNumber("2222 2222 2222 2222");
        card.setExpireTime(LocalDate.parse("2026-02-27"));
        cardRepo.save(card);

        return card;
    }

    @Test
    void testGetAllCards() throws Exception {
        authorization();

        createTestCard();

        mockMvc.perform(get("/api/admin/getAllCards")
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].login").value(TEST_LOGIN))
                .andExpect(jsonPath("$[0].number").value("2222 2222 2222 2222"))
                .andExpect(jsonPath("$[0].expireTime").value("2026-02-27"))
                .andExpect(jsonPath("$[0].balance").value(1000L))
                .andExpect(jsonPath("$[0].cardStatus").value(String.valueOf(CardStatus.ACTIVE)))
                .andExpect(jsonPath("$[0].blockRequest").value(false));
    }

    @Test
    void testBlockCard() throws Exception {
        authorization();

        Long id = createTestCard().getId();

        mockMvc.perform(put("/api/admin/blockCard/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cardStatus").value(String.valueOf(CardStatus.BLOCKED)));
    }

    @Test
    void testDeleteNotExistCard() throws Exception {
        authorization();

        Long id = 10000L;

        mockMvc.perform(delete("/api/admin/deleteCard/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testDeleteNotExistUser() throws Exception {
        authorization();

        Long id = 10000L;

        mockMvc.perform(delete("/api/admin/deleteUser/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testActivateNotExistCard() throws Exception {
        authorization();

        Long id = 10000L;

        mockMvc.perform(put("/api/admin/activateCard/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().is4xxClientError());
    }
}
