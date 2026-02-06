package ru.bank.bank_rest.util;

import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.exception.card.CardNegativeBalanceException;

import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public interface CardUtil {
    static String generateCardNumber() {
        SecureRandom random = new SecureRandom();
        return IntStream.range(0, 16)
                .mapToObj(i -> String.valueOf(random.nextInt(10)))
                .collect(Collectors.joining(""));
    }

    static String encryptCardNumber(String cardNumber) {
        if (cardNumber.length() < 4) {
            throw new IllegalArgumentException();
        }

        return "**** **** **** " + cardNumber.substring(cardNumber.length() - 4);
    }

    static void transferMoney(Card cardFrom, Card cardTo, Long balance) {
        Long cardFromBalance = cardFrom.getBalance();

        if (cardFromBalance - balance < 0) {
            throw new CardNegativeBalanceException(cardFrom.getNumber());
        }

        cardFrom.setBalance(cardFromBalance - balance);
        cardTo.setBalance(cardTo.getBalance() + balance);
    }
}
