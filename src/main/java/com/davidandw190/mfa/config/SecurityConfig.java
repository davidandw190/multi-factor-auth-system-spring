package com.davidandw190.mfa.config;

import com.davidandw190.mfa.services.implementations.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFiler;
    private final AuthenticationProvider authProvider;
    private final LogoutService logoutService;

    @Autowired
    public SecurityConfig(JwtAuthenticationFilter jwtAuthFiler, AuthenticationProvider authProvider, LogoutService logoutService) {
        this.jwtAuthFiler = jwtAuthFiler;
        this.authProvider = authProvider;
        this.logoutService = logoutService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
            .csrf().disable()
            .authorizeHttpRequests()
            .requestMatchers("/auth/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authenticationProvider(authProvider)
            .addFilterBefore(jwtAuthFiler, UsernamePasswordAuthenticationFilter.class)
            .logout()
            .logoutUrl("/auth/logout")
            .addLogoutHandler(logoutService)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext());

         return http.build();
    }

}
