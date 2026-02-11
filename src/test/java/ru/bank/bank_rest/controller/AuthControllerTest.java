package ru.bank.bank_rest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import ru.bank.bank_rest.repository.UserRepo;
import ru.bank.bank_rest.requests.auth.AuthRequest;
import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepo userRepo;

    @AfterEach
    public void resetDb() {
        userRepo.deleteAll();
    }

    private AuthRequest createTestAuthRequest() {
        AuthRequest authRequest = new AuthRequest();
        authRequest.setLogin("TestLogin");
        authRequest.setPassword("123");

        return authRequest;
    }

    @Test
    void testSignupUser() throws Exception {
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(String.valueOf(objectMapper.writeValueAsString(createTestAuthRequest())))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().isOk());
    }

    @Test
    void testWrongContent() throws Exception {
        mockMvc.perform(post("/api/auth/sign-up")
                        .content(String.valueOf(createTestAuthRequest()))
                        .contentType(String.valueOf(MediaType.APPLICATION_JSON)))
                .andExpect(status().is4xxClientError());
    }
}
