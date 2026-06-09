package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.dto.LoginRequest;
import SklepInternetowy.Projekt.dto.RegisterRequest;
import SklepInternetowy.Projekt.service.AuthService;
import SklepInternetowy.Projekt.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            authService.register(request);
            return ResponseEntity.ok(Map.of("message", "Użytkownik utworzony"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        try {
            String token = authService.login(request);
            
            String role = jwtService.getRoleFromToken(token);
            // Zwracamy token użytkownikowi — on go zapisze i będzie wysyłał przy każdym żądaniu
            return ResponseEntity.ok(Map.of("token", token, "role", role));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}