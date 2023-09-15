package com.davidandw190.mfa.services.implementations;

import com.davidandw190.mfa.domain.AuthenticationRequest;
import com.davidandw190.mfa.domain.AuthenticationResponse;
import com.davidandw190.mfa.domain.RegistrationRequest;
import com.davidandw190.mfa.domain.VerificationRequest;
import com.davidandw190.mfa.entities.Token;
import com.davidandw190.mfa.entities.User;
import com.davidandw190.mfa.enums.TokenType;
import com.davidandw190.mfa.repositories.TokenRepository;
import com.davidandw190.mfa.repositories.UserRepository;
import com.davidandw190.mfa.services.AuthenticationService;
import com.davidandw190.mfa.services.JwtService;
import com.davidandw190.mfa.services.TwoFactorAuthenticationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    private final TwoFactorAuthenticationService tfaService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authManager, TokenRepository tokenRepository, TwoFactorAuthenticationService tfaService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authManager = authManager;
        this.tokenRepository = tokenRepository;
        this.tfaService = tfaService;
    }

    /**
     * Registers a new user.
     *
     * @param request Registration request data.
     * @return An AuthenticationResponse with access and refresh tokens.
     */
    @Override
    @Transactional
    public AuthenticationResponse registerUser(RegistrationRequest request) {
        User newRegisteredUser = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .mfaEnabled(request.isMfaEnabled())
                .build();

        Optional<User> possiblyRegisteredUser = userRepository.findUserByEmail(request.getEmail());

        if (possiblyRegisteredUser.isPresent()) {
            throw new RuntimeException("User with email `" + request.getEmail() + "` already exists!");
        }

        if (request.isMfaEnabled()) {
            newRegisteredUser.setSecret(tfaService.generateNewSecret());
        }

        userRepository.save(newRegisteredUser);
        String jwtToken = jwtService.generateToken(newRegisteredUser);
        String refreshToken = jwtService.generateRefreshToken(newRegisteredUser);

        saveUserToken(newRegisteredUser, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(newRegisteredUser.isMfaEnabled())
                .secretImageUri(tfaService.generateQrCodeImageUri(newRegisteredUser.getSecret()))
                .build();
    }

    /**
     * Authenticates a user.
     *
     * @param request Authentication request data.
     * @return An AuthenticationResponse with access and refresh tokens.
     */
    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(
                () -> new RuntimeException("User with email `" + request.getEmail() + "` was not found!")
        );

        if (user.isMfaEnabled()) {
            return AuthenticationResponse.builder()
                    .accessToken("")
                    .refreshToken("")
                    .mfaEnabled(true)
                    .build();
        }
        revokeAllUserTokens(user);
        String jwtToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        saveUserToken(user, jwtToken);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .mfaEnabled(false)
                .build();
    }

    /**
     * Refreshes an access token.
     *
     * @param request  HttpServletRequest for extracting the refresh token.
     * @param response HttpServletResponse for writing the new access token.
     * @throws IOException If there's an error writing to the response.
     */
    @Override
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }

        final String refreshToken = authHeader.substring(7);
        final String userEmail = jwtService.extractUsername(refreshToken);
        if (userEmail != null) {
            var user = userRepository.findUserByEmail(userEmail).orElseThrow(RuntimeException::new);

            if (jwtService.isTokenValid(refreshToken, user)) {
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    @Override
    public AuthenticationResponse verifyCode(VerificationRequest verificationRequest) {
        User toBeVerifiedUser = userRepository.findUserByEmail(verificationRequest.getEmail()).orElseThrow(
                () -> new EntityNotFoundException(String.format("No user found with %S", verificationRequest.getEmail()))
        );

        if (tfaService.isOtpNotValid(toBeVerifiedUser.getSecret(), verificationRequest.getEmail())) {
            throw new BadCredentialsException("Incorrect code!");
        }

        var jwtToken = jwtService.generateToken(toBeVerifiedUser);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .mfaEnabled(toBeVerifiedUser.isMfaEnabled())
                .build();
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

        if (!validTokens.isEmpty()) {
            validTokens.forEach(t -> {
                t.setRevoked(true);
                tokenRepository.save(t);
            });

        }
    }
}
