package ru.bank.bank_rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.bank.bank_rest.dto.CardDto;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.entity.enums.CardStatus;
import ru.bank.bank_rest.exception.card.CardOwnerException;
import ru.bank.bank_rest.mappers.CardMapper;
import ru.bank.bank_rest.repository.CardRepo;
import ru.bank.bank_rest.requests.TransferMoneyRequest;
import ru.bank.bank_rest.util.CardUtil;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CardService {
    private final CardRepo cardRepo;

    @Autowired
    public CardService(CardRepo cardRepo) {
        this.cardRepo = cardRepo;
    }

    public Card getById(Long id) {
        return cardRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));
    }

    public Card getByNumber(String number) {
        return cardRepo.findByNumber(number).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Карты " + number + " не существует"));
    }

    public void save(Card card) {
        cardRepo.save(card);
    }

    public CardDto getCard(Long id) {
        Card card = getById(id);

        return CardMapper.fromCardToCardDto(card);
    }

    public Page<CardDto> getUserCards(User user, Pageable pageable, Optional<String> number) {
        Page<Card> cards = cardRepo.findAllByUser(user, pageable);

        if (number.isPresent()) {
            String cardNumber = number.get();
            cards = new PageImpl<>(cards.stream()
                    .filter(card -> card.getNumber().contains(cardNumber))
                    .collect(Collectors.toList()));
        }

        return new PageImpl<>(CardMapper.fromCardsToCardsDtos(cards.getContent()));
    }

    public Long getBalance(Long id, User user) {
        Card card = cardRepo.findByIdAndUser(id, user).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));

        return card.getBalance();
    }

    @Transactional
    public void transferMoney(TransferMoneyRequest request, User user) {
        String numberFrom = request.getNumberFrom();
        String numberTo = request.getNumberTo();

        Card cardFrom = getByNumber(numberFrom);
        Card cardTo = getByNumber(numberTo);

        if (cardRepo.existsByNumberAndUser(numberFrom, user)) {
            throw new CardOwnerException(numberFrom, user.getLogin());
        }

        if (cardRepo.existsByNumberAndUser(numberTo, user)) {
            throw new CardOwnerException(numberFrom, user.getLogin());
        }

        CardUtil.transferMoney(cardFrom, cardTo, request.getBalance());

        save(cardFrom);
        save(cardTo);
    }

    @Transactional
    public void blockCardRequest(Long id) {
        Card card = getById(id);

        if (card.getCardStatus().equals(CardStatus.BLOCKED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Карта уже заблокирована");
        }

        card.setBlockRequest(true);
        save(card);
    }
}
