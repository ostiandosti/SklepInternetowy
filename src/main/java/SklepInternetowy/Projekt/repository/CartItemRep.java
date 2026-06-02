package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.CartItemEnt;
import SklepInternetowy.Projekt.entity.CartEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRep extends JpaRepository<CartItemEnt, Long> {

    List<CartItemEnt> findByCart(CartEnt cart);
}