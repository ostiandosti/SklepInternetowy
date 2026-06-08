package SklepInternetowy.Projekt.service;

import SklepInternetowy.Projekt.dto.LoginRequest;
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
    private final JwtService jwtService;

    public AuthService(UserRep userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // =========================
    // REGISTER — bez zmian
    // =========================
    public void register(RegisterRequest request) {
        if (request.getPassword().length() < 6) {
            throw new RuntimeException("Hasło musi mieć minimum 6 znaków");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email już istnieje");
        }

        UserEnt user = new UserEnt();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
    }

    // =========================
    // LOGIN — teraz zwraca String (token)
    // =========================
    public String login(LoginRequest request) {
        // Znajdź użytkownika po emailu
        UserEnt user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Nieprawidłowy email lub hasło"));

        // Sprawdź hasło
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Nieprawidłowy email lub hasło");
        }

        // Wygeneruj token i zwróć go — to jest nowość!
        return jwtService.generateToken(user.getEmail());
    }
}