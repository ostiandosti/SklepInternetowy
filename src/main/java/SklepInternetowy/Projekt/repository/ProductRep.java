package SklepInternetowy.Projekt.repository;

import SklepInternetowy.Projekt.entity.ProductEnt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRep extends JpaRepository<ProductEnt, Long> {

}