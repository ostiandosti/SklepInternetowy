package SklepInternetowy.Projekt.config;

import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.UserRep;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AdminInit {

    @Bean
    CommandLineRunner initAdmin(
            UserRep userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            if (!userRepository.existsByEmail("admin@sklep.pl")) {

                UserEnt admin = new UserEnt();

                admin.setUsername("admin");
                admin.setEmail("admin@sklep.pl");
                admin.setPassword(
                        passwordEncoder.encode("admin")
                );
                admin.setRole("ADMIN");

                userRepository.save(admin);
            }
        };
    }
}