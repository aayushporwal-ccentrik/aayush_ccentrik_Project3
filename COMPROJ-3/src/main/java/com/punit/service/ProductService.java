package com.punit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.punit.entities.Product;
import com.punit.repository.IProductPersistance;

@Component
public class ProductService {

    @Autowired
    IProductPersistance product;

    // READ ALL
    public List<Product> ReadAllProducts(){
        return product.findAll();
    }

    // READ ONE
    public Optional<Product> ReadProductById(Long id){
        return product.findById(id);
    }

    // CREATE
    public Product createProduct(Product obj){
        obj.setProductId(null);
        return product.save(obj);
    }

    // UPDATE
    public Product changeProduct(Product payload){

        Optional<Product> myproduct = product.findById(payload.getProductId());

        if(!myproduct.isPresent()){
            return new Product((long)0,"","","","","");
        }

        return product.save(payload);
    }

    // DELETE
    public String deleteProduct(Long id){

        Optional<Product> myproduct = product.findById(id);

        if(!myproduct.isPresent()){
            return "Product Not Found";
        }

        product.deleteById(id);

        return "Product Deleted Successfully";
    }
}