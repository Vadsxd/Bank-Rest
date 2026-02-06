package ru.bank.bank_rest.exception.card;

public class CardNegativeBalanceException extends RuntimeException {
    public CardNegativeBalanceException(String number) {
        super("Card " + number + " has negative balance");
    }
}
