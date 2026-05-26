
package SklepInternetowy.Projekt.controller;


import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.UserRep;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class UserController{

    private final UserRep userRep;

    public UserController(UserRep userRep) {
        this.userRep = userRep;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserEnt user) {

        // sprawdzenie czy email już istnieje
        if (userRep.findAll().stream()
                .anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            return ResponseEntity
                    .badRequest()
                    .body("Email już istnieje");
        }

        // ustawienie domyślnych wartości
        user.setRole("USER");
        user.setCreatedAt(LocalDateTime.now());

        // zapis do bazy
        UserEnt savedUser = userRep.save(user);

        return ResponseEntity.ok(savedUser);
    }
}