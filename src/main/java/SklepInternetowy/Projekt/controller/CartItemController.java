package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.CartItemEnt;
import SklepInternetowy.Projekt.repository.CartItemRep;
import SklepInternetowy.Projekt.entity.CartEnt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@CrossOrigin(origins = "*")
public class CartItemController {

    private final CartItemRep cartItemRep;

    public CartItemController(CartItemRep cartItemRep) {
        this.cartItemRep = cartItemRep;
    }

    // CREATE
    @PostMapping
    public CartItemEnt createCartItem(@RequestBody CartItemEnt item) {
        return cartItemRep.save(item);
    }

    // READ ALL
    @GetMapping
    public List<CartItemEnt> getAllItems() {
        return cartItemRep.findAll();
    }

    //READ BY ID
    @GetMapping("/{id}")
    public CartItemEnt getItemById(@PathVariable Long id) {
        return cartItemRep.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem nie istnieje"));
    }

    //  READ BY CART (ważne!)
    @GetMapping("/cart/{cartId}")
    public List<CartItemEnt> getItemsByCart(@PathVariable Long cartId) {

        CartEnt cart = new CartEnt();
        cart.setId(cartId);

        return cartItemRep.findByCart(cart);
    }

    // UPDATE
    @PutMapping("/{id}")
    public CartItemEnt updateItem(@PathVariable Long id,
                                  @RequestBody CartItemEnt updatedItem) {

        CartItemEnt item = cartItemRep.findById(id)
                .orElseThrow(() -> new RuntimeException("CartItem nie istnieje"));

        item.setProductName(updatedItem.getProductName());
        item.setPrice(updatedItem.getPrice());
        item.setQuantity(updatedItem.getQuantity());
        item.setCart(updatedItem.getCart());

        return cartItemRep.save(item);
    }

    //  DELETE
    @DeleteMapping("/{id}")
    public String deleteItem(@PathVariable Long id) {

        if (!cartItemRep.existsById(id)) {
            throw new RuntimeException("CartItem nie istnieje");
        }

        cartItemRep.deleteById(id);
        return "CartItem usunięty";
    }
}