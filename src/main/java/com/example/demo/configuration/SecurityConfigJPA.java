package com.example.demo.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.demo.service.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfigJPA {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .userDetailsService(userDetailsService)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v3/api-docs/**").permitAll()
                .requestMatchers(
                                "/api/roles/**",
                                "/api/users/**",
                                 "/api/states/**",
                                 "/api/addresses/**",
                                 "/graphql/**").hasRole("ADMIN")
                .requestMatchers("/api/places/**",
                                 "/api/favorites/**",
                                 "/api/reviews/**",
                                 "/api/reviewImages/**",
                                 "/api/services/**",
                                 "/api/serviceReviews/**",
                                 "/api/events/**",
                                 "/api/gastronomies/**",
                                 "/api/transports/**",
                                 "/api/stories/**",
                                 "/api/textAnalysis/**",
                                 "/api/image-analysis/**",
                                 "/api/legends/**",
                                 "/api/municipalities/**").hasAnyRole("USER", "ADMIN")
                .anyRequest().authenticated()
            )
            .httpBasic(basic -> {})  
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
