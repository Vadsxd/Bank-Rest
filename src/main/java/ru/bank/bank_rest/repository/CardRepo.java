package ru.bank.bank_rest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.bank.bank_rest.entity.Card;
import ru.bank.bank_rest.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepo extends JpaRepository<Card, Long> {
    Optional<Card> findByIdAndUser(Long id, User user);
    Optional<Card> findByNumber(String number);
    Page<Card> findAllByUser(User user, Pageable pageable);
    List<Card> findAllByBlockRequest(Boolean blockRequest);
    boolean existsByNumber(String number);
    boolean existsByNumberAndUser(String number, User user);
}
