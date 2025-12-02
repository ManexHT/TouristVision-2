package com.example.demo.service;

import com.example.demo.model.AppUser;
import com.example.demo.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        AppUser user = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));

        // Obtener el rol de la BD (ADMIN o USER)
        String rol = user.getRol().getName();
        System.out.println("USERNAME = " + user.getUsername());
        System.out.println("ROL EN BD = " + rol);

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // ya cifrada en la BD
                .roles(rol) // SPRING convierte "ADMIN" => "ROLE_ADMIN"
                .build();

    }
}

