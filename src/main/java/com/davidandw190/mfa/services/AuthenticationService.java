package com.davidandw190.mfa.services;

import com.davidandw190.mfa.domain.AuthenticationRequest;
import com.davidandw190.mfa.domain.AuthenticationResponse;
import com.davidandw190.mfa.domain.RegistrationRequest;

public interface AuthenticationService {
    AuthenticationResponse registerUser(RegistrationRequest request);

    AuthenticationResponse authenticateUser(AuthenticationRequest request);
}
