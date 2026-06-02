package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.OrderEnt;
import SklepInternetowy.Projekt.entity.UserEnt;
import SklepInternetowy.Projekt.repository.OrderRep;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderRep orderRep;

    public OrderController(OrderRep orderRep) {
        this.orderRep = orderRep;
    }

    // CREATE ORDER
    @PostMapping
    public OrderEnt createOrder(@RequestBody OrderEnt order) {
        order.setCreatedAt(LocalDateTime.now());
        order.setStatus("NEW");
        return orderRep.save(order);
    }

    // GET ALL
    @GetMapping
    public List<OrderEnt> getAll() {
        return orderRep.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public OrderEnt getById(@PathVariable Long id) {
        return orderRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Order nie istnieje"));
    }

    //  GET USER ORDERS
    @GetMapping("/user")
    public List<OrderEnt> getByUser(@RequestBody UserEnt user) {
        return orderRep.findByUser(user);
    }

    // UPDATE STATUS
    @PutMapping("/{id}/status")
    public OrderEnt updateStatus(@PathVariable Long id,
                                 @RequestParam String status) {

        OrderEnt order = orderRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Order nie istnieje"));

        order.setStatus(status);
        return orderRep.save(order);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {

        orderRep.deleteById(id);
        return "Order usunięte";
    }
}