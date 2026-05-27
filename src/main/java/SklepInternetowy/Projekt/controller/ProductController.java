package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.ProductEnt;
import SklepInternetowy.Projekt.repository.ProductRep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductRep productRep;

    // WYŚWIETL WSZYSTKIE PRODUKTY
    @GetMapping
    public List<ProductEnt> getAllProducts() {
        return productRep.findAll();
    }

    // DODAJ PRODUKT
    @PostMapping
    public ProductEnt addProduct(@RequestBody ProductEnt product) {
        return productRep.save(product);
    }

    // USUŃ PRODUKT
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        if (productRep.existsById(id)) {
            productRep.deleteById(id);
            return "Produkt został usunięty";
        }

        return "Produkt o podanym ID nie istnieje";
    }
}