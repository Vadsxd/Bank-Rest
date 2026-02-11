package ru.bank.bank_rest.service;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.entity.enums.Role;
import ru.bank.bank_rest.repository.UserRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private User createTestUser() {
        User user = new User();
        user.setId(1L);
        user.setLogin("Test");
        user.setPassword("123");
        user.setRole(Role.USER);

        return user;
    }

    @Test
    void testGetUserByLogin() {
        User user = createTestUser();
        when(userRepo.findByLogin(user.getLogin())).thenReturn(Optional.of(user));

        User resUser = userService.getByLogin(user.getLogin());

        assertNotNull(resUser);
        assertEquals(user, resUser);
    }
}
