package ru.bank.bank_rest.mappers;

import ru.bank.bank_rest.dto.CardDto;
import ru.bank.bank_rest.entity.Card;

import java.util.List;
import java.util.stream.Collectors;

public interface CardMapper {
    static CardDto fromCardToCardDto(Card card) {
        return CardDto.builder()
                .user(card.getUser())
                .cardStatus(card.getCardStatus())
                .number(card.getNumber())
                .expireTime(card.getExpireTime())
                .blockRequest(card.getBlockRequest())
                .build();
    }

    static List<CardDto> fromCardsToCardsDtos(List<Card> cards) {
        return cards.stream()
                .map(CardMapper::fromCardToCardDto)
                .collect(Collectors.toList());
    }
}
