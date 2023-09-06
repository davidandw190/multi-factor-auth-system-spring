package com.davidandw190.mfa.domain;

import com.davidandw190.mfa.enums.Role;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a new user`s registration request.
 */
@Data
@Builder
public class RegistrationRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Role role;

    public RegistrationRequest() {}

    public RegistrationRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}
