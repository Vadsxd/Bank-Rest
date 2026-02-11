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
import ru.bank.bank_rest.requests.TransferMoneyRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class CardServiceTest {
    @InjectMocks
    private CardService cardService;

    @Mock
    private CardRepo cardRepo;

    @Mock
    private UserRepo userRepo;

    public CardServiceTest() {
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
    void testGetCard() {
        Card card = createTestCard();
        when(cardRepo.findById(card.getId())).thenReturn(Optional.of(card));

        Card resCard = cardService.getById(card.getId());

        assertNotNull(resCard);
        assertEquals(card, resCard);
    }

    @Test
    void testBlockCardRequest() {
        Card card = createTestCard();

        when(cardRepo.findById(card.getId())).thenReturn(Optional.of(card));

        cardService.blockCardRequest(card.getId());

        assertNotNull(card);
        assertEquals(card.getBlockRequest(), true);
    }

    @Test
    void testTransferMoney() {
        User user = createTestUser();
        when(userRepo.findById(user.getId())).thenReturn(Optional.of(user));

        Card cardFrom = createTestCard();
        cardFrom.setUser(user);

        Card cardTo = createTestCard();
        cardTo.setUser(user);
        cardTo.setId(2L);
        cardTo.setNumber("2222 2222 2222 2221");

        user.setUserCards(List.of(cardTo, cardFrom));

        when(cardRepo.findByIdAndUser(cardFrom.getId(), user)).thenReturn(Optional.of(cardFrom));
        when(cardRepo.findByIdAndUser(cardTo.getId(), user)).thenReturn(Optional.of(cardTo));

        when(cardRepo.findByNumber(cardFrom.getNumber())).thenReturn(Optional.of(cardFrom));
        when(cardRepo.findByNumber(cardTo.getNumber())).thenReturn(Optional.of(cardTo));

        TransferMoneyRequest request = new TransferMoneyRequest();
        request.setNumberFrom(cardFrom.getNumber());
        request.setNumberTo(cardTo.getNumber());
        request.setBalance(500L);

        cardService.transferMoney(request, user);

        assertNotNull(cardFrom);
        assertNotNull(cardTo);
        assertEquals(cardFrom.getBalance(), 500L);
        assertEquals(cardTo.getBalance(), 1500L);
    }
}
