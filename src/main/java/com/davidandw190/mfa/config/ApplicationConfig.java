package com.davidandw190.mfa.config;

import com.davidandw190.mfa.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class ApplicationConfig {

    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public UserDetailsService userDetailsService() {

        return username -> userRepository.findUserByEmail(username).orElseThrow(
                () -> new UsernameNotFoundException("No user found with email: " + username)
        );
    }

}
