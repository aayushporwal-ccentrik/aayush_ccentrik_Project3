package com.punit.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.punit.entities.SalesOrderHeader;
import com.punit.repository.ICustomerPersistence;
import com.punit.repository.ISalesOrderHeaderPersistence;

@Component
public class SalesOrderHeaderService {

    @Autowired
    ISalesOrderHeaderPersistence headerRepo;

    @Autowired
    ICustomerPersistence customerRepo;
    
    // READ ALL
    public List<SalesOrderHeader> getAllOrders() {
        return headerRepo.findAll();
    }

    // READ ONE
    public Optional<SalesOrderHeader> getOrderById(Long id) {
        return headerRepo.findById(id);
    }

    public SalesOrderHeader createOrder(SalesOrderHeader obj) {
        // Check 1: customer field must not be null
        if (obj.getCustomer() == null || obj.getCustomer().getCustomerId() == null) {
            throw new RuntimeException("Customer is required to create a Sales Order");
        }

        // Check 2: customer must actually exist in the database
        boolean customerExists = customerRepo.existsById(obj.getCustomer().getCustomerId());
        if (!customerExists) {
            throw new RuntimeException("Customer not found with ID: " + obj.getCustomer().getCustomerId());
        }

        obj.setSalesOrderNumber(null); // let DB auto-generate
        return headerRepo.save(obj);
    }

    // UPDATE
    public SalesOrderHeader updateOrder(SalesOrderHeader payload) {
        Optional<SalesOrderHeader> existing = 
            headerRepo.findById(payload.getSalesOrderNumber());
        if (!existing.isPresent()) {
            return new SalesOrderHeader(); // return empty object if not found
        }
        return headerRepo.save(payload);
    }

    // DELETE
    public String deleteOrder(Long id) {
        Optional<SalesOrderHeader> existing = headerRepo.findById(id);
        if (!existing.isPresent()) {
            return "Sales Order Not Found";
        }
        headerRepo.deleteById(id);
        return "Sales Order Deleted Successfully";
        // Note: cascade = CascadeType.ALL on items means
        // all child items will also be deleted automatically
    }
}