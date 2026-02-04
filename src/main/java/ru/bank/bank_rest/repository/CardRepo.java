package ru.bank.bank_rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.bank_rest.entity.Card;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {
}
