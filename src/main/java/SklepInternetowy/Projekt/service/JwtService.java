
package SklepInternetowy.Projekt.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtService {

    // Wczytuje wartość jwt.secret z application.properties
    @Value("${jwt.secret}")
    private String jwtSecret;

    // Wczytuje czas wygaśnięcia tokenu z application.properties
    @Value("${jwt.expiration}")
    private int jwtExpirationMs;

    // Klucz kryptograficzny — tworzony raz przy starcie aplikacji
    private SecretKey key;

    // @PostConstruct = uruchom tę metodę zaraz po stworzeniu obiektu
    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    // Generuje token JWT dla podanego emaila
    // .setSubject() = kto jest właścicielem tokenu
    // .setIssuedAt() = kiedy token został wydany
    // .setExpiration() = kiedy token wygasa
    // .signWith() = podpisuje token naszym kluczem
    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Wyciąga email (subject) z tokenu
    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Sprawdza czy token jest prawidłowy (nie wygasł, nie był modyfikowany)
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println("Token wygasł: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Nieprawidłowy token: " + e.getMessage());
        }
        return false;
    }
}