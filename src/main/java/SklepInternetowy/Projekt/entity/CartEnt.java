package SklepInternetowy.Projekt.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
public class CartEnt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // właściciel koszyka
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEnt user;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public CartEnt() {
    }

    public CartEnt(UserEnt user, LocalDateTime createdAt) {
        this.user = user;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}