package ru.bank.bank_rest.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.entity.enums.CardStatus;
import ru.bank.bank_rest.entity.enums.Role;
import ru.bank.bank_rest.repository.CardRepo;
import ru.bank.bank_rest.repository.UserRepo;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class AdminServiceTest {
    @InjectMocks
    private AdminService adminService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CardRepo cardRepo;

    public AdminServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("Test");
        user.setPassword("123");
        user.setRole(Role.USER);

        return user;
    }

    private Card createTestCard() {
        Card card = new Card();
        card.setId(1L);
        card.setCardStatus(CardStatus.ACTIVE);
        card.setBalance(1000L);
        card.setBlockRequest(false);
        card.setNumber("2222 2222 2222 2222");
        card.setExpireTime(LocalDate.parse("2026-02-27"));

        return card;
    }

    @Test
    void testGiveAdminRights() {
        User user = createTestUser();
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        adminService.giveAdminRights(user.getId());

        assertNotNull(user);
        assertEquals(Role.ADMIN, user.getRole());
    }

    @Test
    void testDeleteCard() {
        Card card = createTestCard();
        when(cardRepo.findById(card.getId())).thenReturn(Optional.of(card));

        adminService.deleteCard(card.getId());

        assertFalse(cardRepo.existsById(card.getId()));
    }
}
