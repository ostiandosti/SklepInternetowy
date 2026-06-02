package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.CartEnt;
import SklepInternetowy.Projekt.entity.UserEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRep extends JpaRepository<CartEnt, Long> {

    Optional<CartEnt> findByUser(UserEnt user);
}