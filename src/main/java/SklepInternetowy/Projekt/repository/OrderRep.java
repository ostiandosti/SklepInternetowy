package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.OrderEnt;
import SklepInternetowy.Projekt.entity.UserEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRep extends JpaRepository<OrderEnt, Long> {

    List<OrderEnt> findByUser(UserEnt user);
}
