package com.punit.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.punit.entities.Customer;
import com.punit.repository.ICustomerPersistence;

@Service
public class CustomerService {
    
    @Autowired
    ICustomerPersistence customerRepo;
    
    // ─── READ ALL CUSTOMERS ───────────────────────────────
    
    /**
     * Get all customers
     * Used for: READ operation, F4 Help
     */
    public List<Customer> ReadAllCustomers(){
        return customerRepo.findAll();
    }
    
    // ─── READ SINGLE CUSTOMER ─────────────────────────────
    
    /**
     * Get customer by ID
     * Used for: UPDATE operation
     */
    public Optional<Customer> ReadCustomerById(Long id){
        return customerRepo.findById(id);
    }
    
    // ─── CREATE CUSTOMER ──────────────────────────────────
    
    /**
     * Create new customer
     * Auto-generates customerId
     */
    public Customer createCustomer(Customer obj){
        obj.setCustomerId(null);  // Let database auto-generate ID
        return customerRepo.save(obj);
    }
    
    // ─── UPDATE CUSTOMER ──────────────────────────────────
    
    /**
     * Update existing customer
     * Returns empty customer if not found
     */
    public Customer changeCustomer(Customer payload){
        Optional<Customer> existing = customerRepo.findById(payload.getCustomerId());
        if(!existing.isPresent()){
            return new Customer((long)0,"","","","","");
        }
        return customerRepo.save(payload);
    }
    
    // ─── DELETE CUSTOMER ──────────────────────────────────
    
    /**
     * Delete customer by ID
     */
    public String deleteCustomer(Long id){
        Optional<Customer> existing = customerRepo.findById(id);
        if(!existing.isPresent()){
            return "Customer Not Found";
        }
        customerRepo.deleteById(id);
        return "Customer Deleted Successfully";
    }
}