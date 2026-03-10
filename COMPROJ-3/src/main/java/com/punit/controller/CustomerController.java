package com.punit.controller;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.punit.entities.Customer;
import com.punit.service.CustomerService;

@RestController
public class CustomerController {
    @Autowired
    CustomerService cservice;
    
    @GetMapping("/customer")
    public List<Customer> getCustomers(){
        return cservice.ReadAllCustomers();
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long id){
        Optional<Customer> searchResult = cservice.ReadCustomerById(id);
        if(searchResult.isPresent()){
            return ResponseEntity.ok(searchResult.get());
        }else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    
    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer mypostbody){
        return cservice.createCustomer(mypostbody);
    }
    
    @PutMapping("/customer/{customerId}")
    public Customer updateCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestBody Customer customer){
        customer.setCustomerId(customerId);
        return cservice.changeCustomer(customer);
    }
    
    @DeleteMapping("/customer/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") Long id){
        return cservice.deleteCustomer(id);
    }
}