package com.davidandw190.mfa.resources;

import com.davidandw190.mfa.domain.AuthenticationRequest;
import com.davidandw190.mfa.domain.RegistrationRequest;
import com.davidandw190.mfa.domain.AuthenticationResponse;
import com.davidandw190.mfa.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
     * @return ResponseEntity containing an AuthenticationResponse with a token upon successful registration.
     */
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> registerUser(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authService.registerUser(request));
    }

    /**
     * Endpoint for authenticating a user.
     *
     * @param request The authentication request containing user email and password.
     * @return ResponseEntity containing an AuthenticationResponse with a token upon successful authentication.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticateUser(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authService.authenticateUser(request));
    }
}
