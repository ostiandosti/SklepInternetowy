package SklepInternetowy.Projekt.controller;

import SklepInternetowy.Projekt.entity.ProductEnt;
import SklepInternetowy.Projekt.repository.ProductRep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/products")
public class ProductController {
    
    
    private static final Logger log =
            LoggerFactory.getLogger(ProductController.class);


    @Autowired
    private ProductRep productRep;

    // GET ALL
    @GetMapping("/get")
    public List<ProductEnt> getAllProducts() {
        log.info("Pobrano liste wszystkich produtków /product/get");
        return productRep.findAll();
    }

    // ADD PRODUCT (ADMIN)
    @PostMapping
    public ProductEnt addProduct(@RequestBody ProductEnt product) {
        product.setCreatedAt(LocalDateTime.now());
        return productRep.save(product);
    }

    // UPDATE PRODUCT (ADMIN)
    @PutMapping("/{id}")
    public ProductEnt updateProduct(@PathVariable Long id,
                                    @RequestBody ProductEnt updated) {

        ProductEnt product = productRep.findById(id)
                .orElseThrow(() -> new RuntimeException("Produkt nie istnieje"));

        product.setName(updated.getName());
        product.setDescription(updated.getDescription());
        product.setPrice(updated.getPrice());
        product.setQuantity(updated.getQuantity());
        product.setImageUrl(updated.getImageUrl());
        product.setCategory(updated.getCategory());

        return productRep.save(product);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public String deleteProduct(@PathVariable Long id) {

        if (productRep.existsById(id)) {
            productRep.deleteById(id);
            return "Produkt został usunięty";
        }

        return "Produkt o podanym ID nie istnieje";
    }
}