package SklepInternetowy.Projekt.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItemEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private CartEnt cart;

    // ZMIANA: zamiast productName → referencja do produktu
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEnt product;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    public CartItemEnt() {}

    public CartItemEnt(CartEnt cart, ProductEnt product, double price, int quantity) {
        this.cart = cart;
        this.product = product;
        this.price = price;
        this.quantity = quantity;
    }

    // gettery i settery...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public CartEnt getCart() { return cart; }
    public void setCart(CartEnt cart) { this.cart = cart; }

    public ProductEnt getProduct() { return product; }
    public void setProduct(ProductEnt product) { this.product = product; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    // pomocnicze — żeby frontend dalej widział nazwę
    public String getProductName() {
        return product != null ? product.getName() : "";
    }
}