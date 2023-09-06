package com.davidandw190.mfa.config;

import com.davidandw190.mfa.services.implementations.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.davidandw190.mfa.enums.Permission.*;
import static com.davidandw190.mfa.enums.Role.ADMIN;
import static com.davidandw190.mfa.enums.Role.MANAGER;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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

    /**
     * Configures security filters and rules.
     *
     * @param http HttpSecurity instance to configure.
     * @return A configured SecurityFilterChain.
     * @throws Exception If there's an error configuring security.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
         http
                 .csrf().disable()
                 .authorizeHttpRequests()
                 .requestMatchers("/auth/**").permitAll()
//                 .requestMatchers("/management/**").hasAnyRole(ADMIN.name(), MANAGER.name())
//                 .requestMatchers("/admin/**").hasRole(ADMIN.name())
//
//
//                 .requestMatchers(HttpMethod.POST, "/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MANAGEMENT_CREATE.name())
//                 .requestMatchers(HttpMethod.GET, "/management/**").hasAnyAuthority(ADMIN_READ.name(), MANAGEMENT_READ.name())
//                 .requestMatchers(HttpMethod.PUT, "/management/**").hasAnyAuthority(ADMIN_UPDATE.name(), MANAGEMENT_UPDATE.name())
//                 .requestMatchers(HttpMethod.DELETE, "/management/**").hasAnyAuthority(ADMIN_DELETE.name(), MANAGEMENT_DELETE.name())

//                 .requestMatchers(HttpMethod.POST, "/admin/**").hasAuthority(ADMIN_CREATE.name())
//                 .requestMatchers(HttpMethod.GET, "/admin/**").hasAnyAuthority(ADMIN_READ.name())
//                 .requestMatchers(HttpMethod.PUT, "/admin/**").hasAuthority(ADMIN_UPDATE.name())
//                 .requestMatchers(HttpMethod.DELETE, "/admin/**").hasAuthority(ADMIN_DELETE.name())
//

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
