package SklepInternetowy.Projekt.config;

import SklepInternetowy.Projekt.service.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            // STATELESS = serwer nie przechowuje sesji
            // Każde żądanie musi samo udowodnić kim jest (przez token)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .authorizeHttpRequests(auth -> auth
                // Te endpointy są publiczne — nie wymagają tokenu
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()
                // Wszystko inne wymaga tokenu
                .anyRequest().authenticated()
            )

            // Dodaj nasz filtr JWT PRZED domyślnym filtrem Spring Security
            // Kolejność ma znaczenie — najpierw sprawdzamy JWT
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt = algorytm szyfrowania haseł — bardzo bezpieczny
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // AuthenticationManager potrzebny do weryfikacji login/hasło
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}