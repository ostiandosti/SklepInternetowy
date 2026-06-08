package SklepInternetowy.Projekt.service;

import SklepInternetowy.Projekt.entity.*;
import SklepInternetowy.Projekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    // Wstrzykujemy repozytoria — Spring sam je dostarczy
    @Autowired
    private CartRep cartRep;

    @Autowired
    private CartItemRep cartItemRep;

    @Autowired
    private ProductRep productRep;

    // -------------------------------------------------------
    // POMOCNICZA: znajdź koszyk usera lub stwórz nowy
    // -------------------------------------------------------
    // Każdy user ma JEDEN koszyk. Jeśli jeszcze nie ma → tworzymy.
    private CartEnt getOrCreateCart(UserEnt user) {
        // Szukamy koszyka przypisanego do tego usera
        Optional<CartEnt> existing = cartRep.findByUser(user);

        if (existing.isPresent()) {
            // Koszyk już istnieje → zwracamy go
            return existing.get();
        } else {
            // Koszyk nie istnieje → tworzymy nowy
            CartEnt newCart = new CartEnt(user, LocalDateTime.now());
            return cartRep.save(newCart);
        }
    }

    // -------------------------------------------------------
    // DODAJ PRODUKT DO KOSZYKA
    // -------------------------------------------------------
    // productId  → id produktu który user chce kupić
    // quantity   → ile sztuk chce dodać
    // user       → zalogowany user (wiemy to z JWT tokena)
    public String addItem(Long productId, int quantity, UserEnt user) {

        // 1. Znajdź produkt w bazie danych
        Optional<ProductEnt> productOpt = productRep.findById(productId);
        if (productOpt.isEmpty()) {
            return "Produkt nie istnieje";
        }
        ProductEnt product = productOpt.get();

        // 2. Pobierz koszyk usera (lub stwórz nowy)
        CartEnt cart = getOrCreateCart(user);

        // 3. Sprawdź czy ten produkt już jest w koszyku
        List<CartItemEnt> items = cartItemRep.findByCart(cart);
        CartItemEnt existingItem = null;
        for (CartItemEnt item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                existingItem = item;
                break;
            }
        }

        // 4. Oblicz ile już jest w koszyku + ile chcemy dodać
        int alreadyInCart = (existingItem != null) ? existingItem.getQuantity() : 0;
        int totalWanted = alreadyInCart + quantity;

        // 5. WALIDACJA: nie możemy dodać więcej niż jest w magazynie
        if (totalWanted > product.getQuantity()) {
            return "Brak wystarczającej ilości w magazynie. Dostępne: "
                    + product.getQuantity() + ", w koszyku: " + alreadyInCart;
        }

        // 6. Dodaj lub zaktualizuj pozycję w koszyku
        if (existingItem != null) {
            // Produkt już jest w koszyku → tylko zwiększamy ilość
            existingItem.setQuantity(totalWanted);
            cartItemRep.save(existingItem);
        } else {
            // Nowy produkt w koszyku → tworzymy nową pozycję
            // NOWE (działa)
            CartItemEnt newItem = new CartItemEnt(cart, product, product.getPrice().doubleValue(), quantity);
            cartItemRep.save(newItem);
        }

        return "OK";
    }

    // -------------------------------------------------------
    // POBIERZ ZAWARTOŚĆ KOSZYKA
    // -------------------------------------------------------
    public List<CartItemEnt> getItems(UserEnt user) {
        CartEnt cart = getOrCreateCart(user);
        return cartItemRep.findByCart(cart);
    }

    // -------------------------------------------------------
    // ZMIEŃ ILOŚĆ PRODUKTU W KOSZYKU
    // -------------------------------------------------------
    public String updateQuantity(Long itemId, int newQuantity, UserEnt user) {

        // Znajdź pozycję w koszyku
        Optional<CartItemEnt> itemOpt = cartItemRep.findById(itemId);
        if (itemOpt.isEmpty()) {
            return "Pozycja nie istnieje";
        }
        CartItemEnt item = itemOpt.get();

        // Sprawdź czy ta pozycja należy do tego usera (bezpieczeństwo!)
        if (!item.getCart().getUser().getId().equals(user.getId())) {
            return "Brak dostępu";
        }

        // Jeśli ilość = 0 → usuń pozycję
        if (newQuantity <= 0) {
            cartItemRep.delete(item);
            return "OK";
        }

        // Znajdź produkt żeby sprawdzić stan magazynowy
        Optional<ProductEnt> productOpt = productRep.findAll()
                .stream()
                .filter(p -> p.getName().equals(item.getProductName()))
                .findFirst();

        if (productOpt.isPresent()) {
            ProductEnt product = productOpt.get();
            // WALIDACJA stanu magazynowego
            if (newQuantity > product.getQuantity()) {
                return "Brak wystarczającej ilości w magazynie. Dostępne: "
                        + product.getQuantity();
            }
        }

        item.setQuantity(newQuantity);
        cartItemRep.save(item);
        return "OK";
    }

    // -------------------------------------------------------
    // USUŃ JEDEN PRODUKT Z KOSZYKA
    // -------------------------------------------------------
    public String removeItem(Long itemId, UserEnt user) {

        Optional<CartItemEnt> itemOpt = cartItemRep.findById(itemId);
        if (itemOpt.isEmpty()) {
            return "Pozycja nie istnieje";
        }
        CartItemEnt item = itemOpt.get();

        // Sprawdź czy ta pozycja należy do tego usera
        if (!item.getCart().getUser().getId().equals(user.getId())) {
            return "Brak dostępu";
        }

        cartItemRep.delete(item);
        return "OK";
    }

    // -------------------------------------------------------
    // WYCZYŚĆ CAŁY KOSZYK
    // -------------------------------------------------------
    public void clearCart(UserEnt user) {
        CartEnt cart = getOrCreateCart(user);
        List<CartItemEnt> items = cartItemRep.findByCart(cart);
        cartItemRep.deleteAll(items);
    }
}
