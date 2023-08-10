package com.davidandw190.mfa.services.implementations;

import com.davidandw190.mfa.domain.AuthenticationRequest;
import com.davidandw190.mfa.domain.AuthenticationResponse;
import com.davidandw190.mfa.domain.RegistrationRequest;
import com.davidandw190.mfa.entities.User;
import com.davidandw190.mfa.enums.Role;
import com.davidandw190.mfa.repositories.UserRepository;
import com.davidandw190.mfa.services.AuthenticationService;
import com.davidandw190.mfa.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    public AuthenticationResponse registerUser(RegistrationRequest request) {
        User newRegisteredUser = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        userRepository.save(newRegisteredUser);
        String token = jwtService.generateToken(newRegisteredUser);

        return AuthenticationResponse.builder().token(token).build();
    }

    @Override
    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
        return null;
    }
}
