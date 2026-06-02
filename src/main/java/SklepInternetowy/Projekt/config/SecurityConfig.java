package SklepInternetowy.Projekt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        http
                //USTAWIENIA POD TESTY
                .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll()
            );
        // NIE USUWAC TO DEFAULTOWE USTAWIENIA 
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                .requestMatchers(
//                        "/api/auth/register",
//                        "/api/auth/login",
//                        "/api/auth/register",
//                        "/api/auth/login",
//                        "/swagger-ui/**",
//                        "/v3/api-docs/**"
//                ).permitAll()
//                .anyRequest().authenticated()
//                )
//                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
