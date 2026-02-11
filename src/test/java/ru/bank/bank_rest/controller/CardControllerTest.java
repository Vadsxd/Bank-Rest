package ru.bank.bank_rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.entity.enums.CardStatus;
import ru.bank.bank_rest.repository.CardRepo;
import ru.bank.bank_rest.repository.UserRepo;
import ru.bank.bank_rest.requests.auth.AuthRequest;
import ru.bank.bank_rest.service.AuthService;
import ru.bank.bank_rest.service.UserService;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CardControllerTest {
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
        TOKEN = authService.signIn(createTestAuthRequest()).getToken();
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
    void testGetCard() throws Exception {
        authorization();

        Long id = createTestCard().getId();

        mockMvc.perform(get("/api/card/getCard/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.login").value(TEST_LOGIN))
                .andExpect(jsonPath("$.number").value("2222 2222 2222 2222"))
                .andExpect(jsonPath("$.expireTime").value("2026-02-27"))
                .andExpect(jsonPath("$.balance").value(1000L))
                .andExpect(jsonPath("$.cardStatus").value(String.valueOf(CardStatus.ACTIVE)))
                .andExpect(jsonPath("$.blockRequest").value(false));
    }

    @Test
    void testBlockCardRequest() throws Exception {
        authorization();

        Long id = createTestCard().getId();

        mockMvc.perform(put("/api/card/blockCardRequest/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().isOk());
    }

    @Test
    void testGetBalance() throws Exception {
        authorization();

        Long id = createTestCard().getId();

        mockMvc.perform(get("/api/card/getBalance/{id}", id)
                .header("Authorization", "Bearer " + TOKEN))
                .andExpect(content().string("1000"));
    }

    @Test
    void testBlockRequestNotExistCard() throws Exception {
        authorization();

        Long id = 10000L;

        mockMvc.perform(put("/api/card/blockCardRequest/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetBalanceNotExistCard() throws Exception {
        authorization();

        Long id = 10000L;

        mockMvc.perform(put("/api/card/getBalance/{id}", id)
                        .header("Authorization", "Bearer " + TOKEN))
                .andExpect(status().is4xxClientError());
    }
}
