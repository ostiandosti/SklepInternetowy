package SklepInternetowy.Projekt.service;

import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.UserRep;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRep userRepository;

    public CustomUserDetailsService(UserRep userRepository) {
        this.userRepository = userRepository;
    }

    // Spring Security wywołuje tę metodę kiedy chce sprawdzić użytkownika
    // Musimy zwrócić obiekt UserDetails — Spring wie co z nim zrobić
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEnt user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono: " + email));

        // Zamieniamy naszego UserEnt na obiekt który rozumie Spring Security
        // SimpleGrantedAuthority("ROLE_USER") = rola użytkownika
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()))
        );
    }
}