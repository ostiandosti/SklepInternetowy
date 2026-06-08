
package SklepInternetowy.Projekt.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {
    // OncePerRequestFilter = filtr który uruchamia się RAZ na każde żądanie HTTP

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, CustomUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Pobierz nagłówek "Authorization" z żądania
        String header = request.getHeader("Authorization");

        // 2. Token powinien wyglądać tak: "Bearer eyJhbGciOi..."
        //    Sprawdzamy czy nagłówek istnieje i zaczyna się od "Bearer "
        if (header == null || !header.startsWith("Bearer ")) {
            // Brak tokenu — przepuść żądanie dalej (może to jest /login lub /register)
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Wytnij słowo "Bearer " (7 znaków) i zostaw sam token
        String token = header.substring(7);

        // 4. Sprawdź czy token jest prawidłowy
        if (jwtService.validateToken(token)) {

            // 5. Wyciągnij email z tokenu
            String email = jwtService.getEmailFromToken(token);

            // 6. Załaduj użytkownika z bazy danych
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 7. Stwórz obiekt "uwierzytelnienia" — Spring Security go rozumie
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,                          // hasło — null bo już zweryfikowane przez token
                            userDetails.getAuthorities()   // role użytkownika
                    );

            authentication.setDetails(
                    new WebAuthenticationDetailsSource().buildDetails(request)
            );

            // 8. Powiedz Spring Security: "Ten użytkownik jest zalogowany"
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 9. Puść żądanie dalej — do kontrolera
        filterChain.doFilter(request, response);
    }
}