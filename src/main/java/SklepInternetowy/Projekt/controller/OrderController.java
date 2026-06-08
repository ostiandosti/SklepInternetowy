package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.OrderEnt;
import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.UserRep;
import SklepInternetowy.Projekt.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserRep userRep;

    // pomocnicza — taka sama jak w CartController
    private UserEnt getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        return userRep.findByEmail(email).orElseThrow();
    }

    // -------------------------------------------------------
    // POST /orders/place
    // Zamawia wszystko co jest w koszyku
    // -------------------------------------------------------
    @PostMapping("/place")
    public ResponseEntity<String> placeOrder(Authentication authentication) {
        UserEnt user = getCurrentUser(authentication);
        String result = orderService.placeOrder(user);

        if (result.equals("OK")) {
            return ResponseEntity.ok("Zamówienie złożone!");
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    // -------------------------------------------------------
    // GET /orders/history
    // Zwraca listę zamówień zalogowanego usera
    // -------------------------------------------------------
    @GetMapping("/history")
    public List<OrderEnt> getHistory(Authentication authentication) {
        UserEnt user = getCurrentUser(authentication);
        return orderService.getOrders(user);
    }
}