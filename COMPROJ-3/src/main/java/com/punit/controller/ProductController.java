package com.punit.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.punit.entities.Product;
import com.punit.service.ProductService;

@RestController
public class ProductController {
    @Autowired
    ProductService pservice;
    
    // GET ALL PRODUCTS
    @GetMapping("/product")
    public List<Product> getProduct(){
        return pservice.ReadAllProducts();
    }
    
    // GET PRODUCT BY ID
    @GetMapping("/product/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Long code){
        Optional<Product> searchResult = pservice.ReadProductById(code);
        if(searchResult.isPresent()) {
            return ResponseEntity.ok(searchResult.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
  
    
    // CREATE PRODUCT
    @PostMapping("/product")
    public Product createProduct(@RequestBody Product mypostbody){
        return pservice.createProduct(mypostbody);
    }
    
    //Added {productId} to URL
    @PutMapping("/product/{productId}")
    public Product updateProduct(
            @PathVariable("productId") Long productId,
            @RequestBody Product product){
        product.setProductId(productId);
        return pservice.changeProduct(product);
    }
    
    // DELETE PRODUCT
    @DeleteMapping("/product/{productId}")
    public String deleteProduct(@PathVariable("productId") Long id){
        return pservice.deleteProduct(id);
    }
}