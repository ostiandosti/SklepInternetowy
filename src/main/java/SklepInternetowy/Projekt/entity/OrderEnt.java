package SklepInternetowy.Projekt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class OrderEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // kto zamówił
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEnt user;

    @Column(nullable = false)
    private double totalPrice;

    @Column(nullable = false)
    private String status; // NEW, PAID, SHIPPED

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public OrderEnt() {}

    public OrderEnt(UserEnt user, double totalPrice, String status, LocalDateTime createdAt) {
        this.user = user;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }

    // GETTERY I SETTERY

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEnt getUser() {
        return user;
    }

    public void setUser(UserEnt user) {
        this.user = user;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}