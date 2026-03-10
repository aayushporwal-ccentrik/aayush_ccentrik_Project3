package com.punit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.punit.entities.Customer;
import com.punit.repository.ICustomerPersistence;

@Component
public class CustomerService {

    @Autowired
    ICustomerPersistence customerRepo;

    // READ ALL
    public List<Customer> ReadAllCustomers(){
        return customerRepo.findAll();
    }

    // READ ONE
    public Optional<Customer> ReadCustomerById(Long id){
        return customerRepo.findById(id);
    }

    // CREATE
    public Customer createCustomer(Customer obj){
        obj.setCustomerId(null);
        return customerRepo.save(obj);
    }

    // UPDATE
    public Customer changeCustomer(Customer payload){
        Optional<Customer> existing = customerRepo.findById(payload.getCustomerId());

        if(!existing.isPresent()){
            return new Customer((long)0,"","","","","");
        }

        return customerRepo.save(payload);
    }

    // DELETE
    public String deleteCustomer(Long id){
        Optional<Customer> existing = customerRepo.findById(id);

        if(!existing.isPresent()){
            return "Customer Not Found";
        }

        customerRepo.deleteById(id);
        return "Customer Deleted Successfully";
    }
}