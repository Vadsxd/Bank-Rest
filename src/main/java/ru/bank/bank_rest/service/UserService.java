package ru.bank.bank_rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.repository.UserRepo;

@Service
public class UserService {
    private final UserRepo userRepo;

    @Autowired
    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public void save(User user) {
        userRepo.save(user);
    }

    @Transactional
    public void create(User user) {
        if (userRepo.existsByLogin(user.getLogin())) {
            throw new RuntimeException("Пользователь с таким логином уже существует");
        }

        save(user);
    }

    public User getByLogin(String login) {
        return userRepo.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByLogin;
    }

    public User getCurrentUser() {
        var login = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByLogin(login);
    }
}
