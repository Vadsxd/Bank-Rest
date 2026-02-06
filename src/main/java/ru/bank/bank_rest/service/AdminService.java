package ru.bank.bank_rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.bank.bank_rest.dto.CardDto;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.entity.enums.CardStatus;
import ru.bank.bank_rest.entity.enums.Role;
import ru.bank.bank_rest.mappers.CardMapper;
import ru.bank.bank_rest.repository.CardRepo;
import ru.bank.bank_rest.repository.UserRepo;
import ru.bank.bank_rest.requests.CreateCardRequest;
import ru.bank.bank_rest.util.CardUtil;

import java.util.List;
import java.util.Objects;

@Service
public class AdminService {
    private final UserRepo userRepo;
    private final CardRepo cardRepo;

    @Autowired
    public AdminService(UserRepo userRepo, CardRepo cardRepo) {
        this.userRepo = userRepo;
        this.cardRepo = cardRepo;
    }

    @Transactional
    public Card createCard(CreateCardRequest request) {
        User user = userRepo.findById(request.getUserId()).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный пользователь не зарегистрирован"));

        String cardNumber = CardUtil.generateCardNumber();

        while (cardRepo.existsByNumber(cardNumber)) {
            cardNumber = CardUtil.generateCardNumber();
        }

        String encryptedCardNumber = CardUtil.encryptCardNumber(cardNumber);

        Card card = Card.builder()
                .user(user)
                .number(encryptedCardNumber)
                .expireTime(request.getExpireTime())
                .cardStatus(CardStatus.ACTIVE)
                .balance(request.getBalance())
                .blockRequest(false)
                .build();

        cardRepo.save(card);

        return card;
    }

    @Transactional
    public Card blockCard(Long id) {
        Card card = cardRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));

        if (card.getCardStatus().equals(CardStatus.BLOCKED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Карта уже заблокирована");
        }

        card.setCardStatus(CardStatus.BLOCKED);
        card.setBlockRequest(false);
        cardRepo.save(card);

        return card;
    }

    @Transactional
    public Card activateCard(Long id) {
        Card card = cardRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));

        if (card.getCardStatus().equals(CardStatus.ACTIVE)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Карта уже активирована");
        }

        card.setCardStatus(CardStatus.ACTIVE);
        cardRepo.save(card);

        return card;
    }

    @Transactional
    public void deleteCard(Long id) {
        Card card = cardRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));

        cardRepo.delete(card);
    }

    @Transactional
    public void deleteUser(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный пользователь не зарегистрирован"));

        userRepo.delete(user);
    }

    @Transactional
    public void giveAdminRights(Long id) {
        User user = userRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данный пользователь не зарегистрирован"));

        if (user.getRole().equals(Role.ADMIN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Пользователь уже Админ");
        }

        user.setRole(Role.ADMIN);
        userRepo.save(user);
    }

    public List<CardDto> getBlockRequestedCards() {
        List<Card> cards = cardRepo.findAllByBlockRequest(true);

        return CardMapper.fromCardsToCardsDtos(cards);
    }

    public List<CardDto> getAllCards() {
        List<Card> cards = cardRepo.findAll();

        return CardMapper.fromCardsToCardsDtos(cards);
    }
}
