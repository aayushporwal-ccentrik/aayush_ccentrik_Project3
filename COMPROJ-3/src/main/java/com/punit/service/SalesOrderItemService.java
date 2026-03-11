package com.punit.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.punit.entities.SalesOrderHeader;
import com.punit.entities.SalesOrderItem;
import com.punit.repository.ISalesOrderHeaderPersistence;
import com.punit.repository.ISalesOrderItemPersistence;

@Component
public class SalesOrderItemService {

    @Autowired
    ISalesOrderItemPersistence itemRepo;
    
    @Autowired
    ISalesOrderHeaderPersistence headerRepo;

    // READ ALL
    public List<SalesOrderItem> getAllItems() {
        return itemRepo.findAll();
    }


    // READ ONE
    public Optional<SalesOrderItem> getItemById(Long id) {
        return itemRepo.findById(id);
    }

    // CREATE
    public SalesOrderItem createItem(SalesOrderItem obj) {
        obj.setSoItemId(null);
        Long soNumber = obj.getSalesOrderHeader().getSalesOrderNumber();       
        SalesOrderHeader realHeader = headerRepo.findById(soNumber)
            .orElseThrow(() -> new RuntimeException("Sales Order not found: " + soNumber));
        obj.setSalesOrder(realHeader);  
        return itemRepo.save(obj);
    }

    // UPDATE — Only quantity can be changed
    public SalesOrderItem updateItem(Long id, SalesOrderItem payload) {  // ✅ fixed
        SalesOrderItem existing = itemRepo.findById(id)                   // ✅ fixed
            .orElseThrow(() -> new RuntimeException("Item not found: " + id));
        existing.setQuantity(payload.getQuantity());
        return itemRepo.save(existing);
    }}