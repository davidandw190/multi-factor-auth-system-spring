package com.davidandw190.mfa.resources;

import com.davidandw190.mfa.domain.AuthenticationRequest;
import com.davidandw190.mfa.domain.RegistrationRequest;
import com.davidandw190.mfa.domain.AuthenticationResponse;
import com.davidandw190.mfa.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * REST controller for handling authentication-related endpoints.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationResource {

    private final AuthenticationService authService;

    @Autowired
    public AuthenticationResource(AuthenticationService authService) {
        this.authService = authService;
    }

    /**
     * Endpoint for registering a new user.
     *
     * @param request The registration request containing user information.
     * @return ResponseEntity containing an AuthenticationResponse with an auth and refresh token upon success.
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequest request) {
        var response = authService.registerUser(request);
        if (request.isMfaEnabled()) {
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.accepted().build();

    }

    /**
     * Endpoint for authenticating an existing user.
     *
     * @param request The authentication request containing user email and password.
     * @return ResponseEntity containing an AuthenticationResponse with an auth and refresh token upon success.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.refreshToken(request, response);

    }

}
