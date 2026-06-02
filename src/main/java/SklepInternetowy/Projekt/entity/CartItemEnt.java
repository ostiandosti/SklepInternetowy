package SklepInternetowy.Projekt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItemEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // do jakiego koszyka należy
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEnt cart;

    // nazwa produktu (na razie prosto)
    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    public CartItemEnt() {
    }

    public CartItemEnt(CartEnt cart, String productName, double price, int quantity) {
        this.cart = cart;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
    }

    // GETTERY I SETTERY

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CartEnt getCart() {
        return cart;
    }

    public void setCart(CartEnt cart) {
        this.cart = cart;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}