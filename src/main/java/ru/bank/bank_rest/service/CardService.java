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

    public CardDto getCard(Long id) {
        Card card = cardRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));

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

        Card cardFrom = cardRepo.findByNumber(numberFrom).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Карты-отправителя не существует"));

        Card cardTo = cardRepo.findByNumber(numberTo).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Карты-принимателя не существует"));

        if (cardRepo.existsByNumberAndUser(numberFrom, user)) {
            throw new CardOwnerException(numberFrom, user.getLogin());
        }

        if (cardRepo.existsByNumberAndUser(numberTo, user)) {
            throw new CardOwnerException(numberFrom, user.getLogin());
        }

        CardUtil.transferMoney(cardFrom, cardTo, request.getBalance());

        cardRepo.save(cardFrom);
        cardRepo.save(cardTo);
    }

    @Transactional
    public void blockCardRequest(Long id) {
        Card card = cardRepo.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Данной карты не существует"));

        if (card.getCardStatus().equals(CardStatus.BLOCKED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Карта уже заблокирована");
        }

        card.setBlockRequest(true);
        cardRepo.save(card);
    }
}
