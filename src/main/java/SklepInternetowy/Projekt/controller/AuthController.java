package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.dto.LoginRequest;
import SklepInternetowy.Projekt.dto.RegisterRequest;
import SklepInternetowy.Projekt.service.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(
            @RequestBody RegisterRequest request) {

        authService.register(request);

        return "Użytkownik utworzony";
    }

    @PostMapping("/login")
    public String login(
            @RequestBody LoginRequest request) {

        return "Endpoint logowania";
    }
}