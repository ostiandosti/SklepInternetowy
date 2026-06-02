package SklepInternetowy.Projekt.service;

import SklepInternetowy.Projekt.dto.RegisterRequest;
import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.UserRep;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final UserRep userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRep userRepository,
                       PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email już istnieje");
        }

        UserEnt user = new UserEnt();

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole("USER");

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }
}