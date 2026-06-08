package SklepInternetowy.Projekt.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity
@Table(name = "order_items")
public class OrderItemEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    

    @JsonIgnore   
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEnt order;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int quantity;

    public OrderItemEnt() {
    }

    public OrderItemEnt(OrderEnt order, String productName, double price, int quantity) {
        this.order = order;
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

    public OrderEnt getOrder() {
        return order;
    }

    public void setOrder(OrderEnt order) {
        this.order = order;
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