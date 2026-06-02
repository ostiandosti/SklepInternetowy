package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.CartEnt;
import SklepInternetowy.Projekt.repository.CartRep;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin(origins = "*")
public class CartController {

    private final CartRep cartRep;

    public CartController(CartRep cartRep) {
        this.cartRep = cartRep;
    }

    //  CREATE
    @PostMapping
    public CartEnt createCart(@RequestBody CartEnt cart) {
        return cartRep.save(cart);
    }

    // READ ALL
    @GetMapping
    public List<CartEnt> getAllCarts() {
        return cartRep.findAll();
    }

    // READ BY ID
    @GetMapping("/{id}")
    public CartEnt getCartById(@PathVariable Long id) {
        return cartRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart nie istnieje"));
    }

    //  UPDATE
    @PutMapping("/{id}")
    public CartEnt updateCart(@PathVariable Long id,
                              @RequestBody CartEnt updatedCart) {

        CartEnt cart = cartRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart nie istnieje"));

        cart.setUser(updatedCart.getUser());
        cart.setCreatedAt(updatedCart.getCreatedAt());

        return cartRep.save(cart);
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public String deleteCart(@PathVariable Long id) {

        if (!cartRep.existsById(id)) {
            throw new RuntimeException("Cart nie istnieje");
        }

        cartRep.deleteById(id);
        return "Cart usunięty";
    }
}
