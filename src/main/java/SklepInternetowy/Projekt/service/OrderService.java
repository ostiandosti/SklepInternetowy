package SklepInternetowy.Projekt.service;

import SklepInternetowy.Projekt.entity.*;
import SklepInternetowy.Projekt.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderRep orderRep;

    @Autowired
    private OrderItemRep orderItemRep;

    @Autowired
    private ProductRep productRep; // <-- NOWE: potrzebne do zapisu ilości

    // -------------------------------------------------------
    // Tworzy zamówienie z koszyka usera
    // -------------------------------------------------------
    public String placeOrder(UserEnt user) {

        // 1. Pobierz zawartość koszyka
        List<CartItemEnt> items = cartService.getItems(user);

        // 2. Jeśli koszyk jest pusty → błąd
        if (items.isEmpty()) {
            return "Koszyk jest pusty";
        }

        // 3. NOWE: Sprawdź czy każdy produkt ma wystarczającą ilość w magazynie
        //    Robimy to PRZED stworzeniem zamówienia — po co tworzyć zamówienie
        //    jeśli i tak nie możemy go zrealizować?
        for (CartItemEnt item : items) {
            ProductEnt product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                // Zwracamy błąd z nazwą produktu którego brakuje
                return "Brak wystarczającej ilości produktu: " + product.getName();
            }
        }

        // 3. Policz łączną cenę
        double total = 0;
        for (CartItemEnt item : items) {
            total += item.getPrice() * item.getQuantity();
        }

        // 4. Stwórz nowe zamówienie i zapisz je do bazy
        OrderEnt order = new OrderEnt(user, total, "NEW", LocalDateTime.now());
        orderRep.save(order);

        // 5. Dla każdej pozycji z koszyka → stwórz OrderItem i zapisz
        for (CartItemEnt item : items) {
            OrderItemEnt orderItem = new OrderItemEnt(
                    order,
                    item.getProduct().getName(), // zapamiętujemy nazwę produktu
                    item.getPrice(),
                    item.getQuantity()
            );
            orderItemRep.save(orderItem);
            // NOWE: odejmij zakupioną ilość od stanu magazynowego
            ProductEnt product = item.getProduct();
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRep.save(product); // zapisz zmianę do bazy
        }

        // 6. Wyczyść koszyk
        cartService.clearCart(user);

        return "OK";
    }

    // -------------------------------------------------------
    // Zwraca historię zamówień usera
    // -------------------------------------------------------
    public List<OrderEnt> getOrders(UserEnt user) {
        return orderRep.findByUser(user);
    }

    public OrderEnt getOrderById(Long id) {
        return orderRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Zamówienie nie istnieje"));
    }
}
