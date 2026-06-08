package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.OrderItemEnt;
import org.springframework.data.jpa.repository.JpaRepository;



public interface OrderItemRep extends JpaRepository<OrderItemEnt, Long> {


}