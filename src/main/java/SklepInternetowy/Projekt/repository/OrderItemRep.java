package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.OrderItemEnt;
import SklepInternetowy.Projekt.entity.OrderEnt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRep extends JpaRepository<OrderItemEnt, Long> {

    List<OrderItemEnt> findByOrder(OrderEnt order);
}