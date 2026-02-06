package ru.bank.bank_rest.exception.card;

public class CardOwnerException extends RuntimeException {
    public CardOwnerException(String number, String login) {
        super("Card " + number + " not belong to user " + login);
    }
}
