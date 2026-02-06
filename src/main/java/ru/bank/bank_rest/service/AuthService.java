package ru.bank.bank_rest.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.bank.bank_rest.entity.User;
import ru.bank.bank_rest.entity.enums.Role;
import ru.bank.bank_rest.requests.auth.AuthRequest;
import ru.bank.bank_rest.responses.AuthResponse;
import ru.bank.bank_rest.security.JwtProvider;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(
            UserService userService,
            JwtProvider jwtProvider,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager
    ) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse signUp(AuthRequest request) {

        User user = User.builder()
                .login(request.getLogin())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userService.create(user);

        String jwt = jwtProvider.generateToken(user);
        return new AuthResponse(jwt);
    }

    public AuthResponse signIn(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getLogin(),
                request.getPassword()
        ));

        UserDetails user = userService
                .userDetailsService()
                .loadUserByUsername(request.getLogin());

        String jwt = jwtProvider.generateToken(user);
        return new AuthResponse(jwt);
    }
}
