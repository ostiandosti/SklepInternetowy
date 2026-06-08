package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.CartItemEnt;
import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.UserRep;
import SklepInternetowy.Projekt.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/cart")
public class CartController {
    
       
    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);


    @Autowired
    private CartService cartService;

    @Autowired
    private UserRep userRep;

    // -------------------------------------------------------
    // POMOCNICZA: pobierz usera z tokena JWT
    // -------------------------------------------------------
    // Spring Security po weryfikacji tokena wstrzykuje obiekt Authentication.
    // authentication.getName() zwraca email usera (tak skonfigurowałeś JWT).
    // Na tej podstawie pobieramy usera z bazy danych.
    private UserEnt getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRep.findByEmail(email).orElseThrow();
    }

    // -------------------------------------------------------
    // GET /cart
    // Zwraca zawartość koszyka zalogowanego usera
    // -------------------------------------------------------
    @GetMapping
    public List<CartItemEnt> getCart(Authentication authentication) {
        UserEnt user = getCurrentUser(authentication);
        return cartService.getItems(user);
    }

    // -------------------------------------------------------
    // POST /cart/add
    // Dodaje produkt do koszyka
    //
    // Ciało żądania (JSON): { "productId": 1, "quantity": 2 }
    // -------------------------------------------------------
    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestBody Map<String, Object> body,
            Authentication authentication) {

        // Wyciągamy dane z JSON-a
        Long productId = Long.valueOf(body.get("productId").toString());
        int quantity = Integer.parseInt(body.get("quantity").toString());

        UserEnt user = getCurrentUser(authentication);
        String result = cartService.addItem(productId, quantity, user);

        if (result.equals("OK")) {
            return ResponseEntity.ok("Dodano do koszyka");
        } else {
            // 400 Bad Request → coś poszło nie tak (np. brak w magazynie)
            return ResponseEntity.badRequest().body(result);
        }
    }

    // -------------------------------------------------------
    // PUT /cart/update/{itemId}
    // Zmienia ilość produktu w koszyku
    //
    // Ciało żądania (JSON): { "quantity": 3 }
    // -------------------------------------------------------
    @PutMapping("/update/{itemId}")
    public ResponseEntity<String> updateItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> body,
            Authentication authentication) {

        int newQuantity = Integer.parseInt(body.get("quantity").toString());
        UserEnt user = getCurrentUser(authentication);
        String result = cartService.updateQuantity(itemId, newQuantity, user);

        if (result.equals("OK")) {
            return ResponseEntity.ok("Zaktualizowano");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // -------------------------------------------------------
    // DELETE /cart/remove/{itemId}
    // Usuwa jeden produkt z koszyka
    // -------------------------------------------------------
    @DeleteMapping("/remove/{itemId}")
    public ResponseEntity<String> removeItem(
            @PathVariable Long itemId,
            Authentication authentication) {

        UserEnt user = getCurrentUser(authentication);
        String result = cartService.removeItem(itemId, user);

        if (result.equals("OK")) {
            return ResponseEntity.ok("Usunięto");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // -------------------------------------------------------
    // DELETE /cart/clear
    // Czyści cały koszyk
    // -------------------------------------------------------
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearCart(Authentication authentication) {
        UserEnt user = getCurrentUser(authentication);
        cartService.clearCart(user);
        log.info("Koszyk został wyczyszczony /cart/clear");
        return ResponseEntity.ok("Koszyk wyczyszczony");
    }
}