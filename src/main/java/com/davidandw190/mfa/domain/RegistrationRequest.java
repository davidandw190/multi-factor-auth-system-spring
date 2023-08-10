package com.davidandw190.mfa.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistrationRequest {

    private String firstname;
    private String lastname;
    private String email;
    private String password;

    public RegistrationRequest() {}

    public RegistrationRequest(String firstname, String lastname, String email, String password) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }
}
