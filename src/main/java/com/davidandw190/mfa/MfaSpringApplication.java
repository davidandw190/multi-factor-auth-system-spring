package com.davidandw190.mfa;

import com.davidandw190.mfa.domain.RegistrationRequest;
import com.davidandw190.mfa.enums.Role;
import com.davidandw190.mfa.services.AuthenticationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MfaSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(MfaSpringApplication.class, args);
	}

}
