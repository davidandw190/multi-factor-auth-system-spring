package com.davidandw190.mfa.services.implementations;

import com.davidandw190.mfa.domain.AuthenticationRequest;
import com.davidandw190.mfa.domain.AuthenticationResponse;
import com.davidandw190.mfa.domain.RegistrationRequest;
import com.davidandw190.mfa.entities.Token;
import com.davidandw190.mfa.entities.User;
import com.davidandw190.mfa.enums.Role;
import com.davidandw190.mfa.enums.TokenType;
import com.davidandw190.mfa.repositories.TokenRepository;
import com.davidandw190.mfa.repositories.UserRepository;
import com.davidandw190.mfa.services.AuthenticationService;
import com.davidandw190.mfa.services.JwtService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of the AuthenticationService interface for user authentication and registration.
 */
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authManager;
    private final TokenRepository tokenRepository;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authManager, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.tokenRepository = tokenRepository;
    }


    /**
     * Registers a new user.
     *
     * @param request The registration request containing user information.
     * @return AuthenticationResponse containing a token upon successful registration.
     * @throws RuntimeException If a user with the provided email already exists.
     */
    @Override
    @Transactional
    public AuthenticationResponse registerUser(RegistrationRequest request) {
        User newRegisteredUser = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        Optional<User> possiblyRegisteredUser = userRepository.findUserByEmail(request.getEmail());

        if (possiblyRegisteredUser.isPresent()) {
            throw new RuntimeException("User with email `" + request.getEmail() + "` already exists!");
        }

        userRepository.save(newRegisteredUser);
        String jwtToken = jwtService.generateToken(newRegisteredUser);

        saveUserToken(newRegisteredUser, jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    /**
     * Authenticates a user.
     *
     * @param request The authentication request containing user email and password.
     * @return AuthenticationResponse containing a token upon successful authentication.
     * @throws RuntimeException If the user is not found or authentication fails.
     */
    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {

        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(
                () -> new RuntimeException("User with email `" + request.getEmail() + "` was not found!")
        );

        revokeAllUserTokens(user);
        String jwtToken = jwtService.generateToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder().token(jwtToken).build();

    }

    private void saveUserToken(User savedUser, String jwtToken) {
        Token token = Token.builder()
                .user(savedUser)
                .token(jwtToken)
                .type(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());

        if (!(validTokens.isEmpty())) {
            validTokens.forEach(t -> { t.setRevoked(true); });
        }
    }

}
