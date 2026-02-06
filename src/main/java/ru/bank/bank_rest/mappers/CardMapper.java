package ru.bank.bank_rest.mappers;

import ru.bank.bank_rest.dto.CardDto;
import ru.bank.bank_rest.entity.Card;

import java.util.List;
import java.util.stream.Collectors;

public interface CardMapper {
    static CardDto fromCardToCardDto(Card card) {
        return CardDto.builder()
                .login(card.getUser().getLogin())
                .cardStatus(card.getCardStatus())
                .number(card.getNumber())
                .expireTime(card.getExpireTime())
                .balance(card.getBalance())
                .blockRequest(card.getBlockRequest())
                .build();
    }

    static List<CardDto> fromCardsToCardsDtos(List<Card> cards) {
        return cards.stream()
                .map(CardMapper::fromCardToCardDto)
                .collect(Collectors.toList());
    }
}
