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
    private CustomerService customerService;

    // ─── F4 HELP ENDPOINT ─────────────────────────────────────
    
    /**
     * ✅ F4 Help endpoint - returns all customers for value help dialog
     * Called when user clicks F4 button on Customer field in Sales Order
     * Returns: List of all customers with customerId and name (for display)
     */
    @GetMapping("/customers/f4help")
    public List<Customer> getCustomerListForF4Help() {
        return customerService.ReadAllCustomers();  // ✅ Correct method name
    }

    // ─── CRUD OPERATIONS ──────────────────────────────────────
    
    /**
     * Get all customers
     * Endpoint: GET /customer
     */
    @GetMapping("/customer")
    public List<Customer> getAllCustomers() {
        return customerService.ReadAllCustomers();  // ✅ Correct method name
    }

    /**
     * Get single customer by ID
     * Endpoint: GET /customer/{customerId}
     */
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable("customerId") Long id) {
        Optional<Customer> searchResult = customerService.ReadCustomerById(id);  // ✅ Correct method name
        if(searchResult.isPresent()){
            return ResponseEntity.ok(searchResult.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    /**
     * Create new customer
     * Endpoint: POST /customer
     * Body: { "name": "...", "city": "...", "state": "...", "phone": "...", "address": "..." }
     */
    @PostMapping("/customer")
    public Customer createCustomer(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);  // ✅ Correct method name
    }

    /**
     * Update existing customer
     * Endpoint: PUT /customer/{customerId}
     * Body: { "name": "...", "city": "...", "state": "...", "phone": "...", "address": "..." }
     */
    @PutMapping("/customer/{customerId}")
    public Customer updateCustomer(
            @PathVariable("customerId") Long customerId,
            @RequestBody Customer customer) {
        customer.setCustomerId(customerId);
        return customerService.changeCustomer(customer);  // ✅ Correct method name
    }

    /**
     * Delete customer
     * Endpoint: DELETE /customer/{customerId}
     */
    @DeleteMapping("/customer/{customerId}")
    public String deleteCustomer(@PathVariable("customerId") Long id) {
        return customerService.deleteCustomer(id);
    }
}