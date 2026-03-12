
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

    // READ ONE — by itemNumber (the @Id)
    // ✅ CHANGED: parameter type Integer, method name reflects itemNumber key
    public Optional<SalesOrderItem> getItemByItemNumber(Integer itemNumber) {
        return itemRepo.findById(itemNumber);
    }

    // CREATE
    public SalesOrderItem createItem(SalesOrderItem obj) {
        // ✅ Null out itemNumber so DB auto-generates it (@GeneratedValue)
        obj.setItemNumber(null);

        Long soNumber = obj.getSalesOrderHeader().getSalesOrderNumber();
        SalesOrderHeader realHeader = headerRepo.findById(soNumber)
            .orElseThrow(() -> new RuntimeException("Sales Order not found: " + soNumber));

        // ✅ setSalesOrderHeader() — renamed from setSalesOrder()
        obj.setSalesOrderHeader(realHeader);

        return itemRepo.save(obj);
    }

    // UPDATE — only quantity can be changed
    // ✅ CHANGED: id is now itemNumber (Integer)
    public SalesOrderItem updateItem(Integer itemNumber, SalesOrderItem payload) {
        SalesOrderItem existing = itemRepo.findById(itemNumber)
            .orElseThrow(() -> new RuntimeException("Item not found: " + itemNumber));
        existing.setQuantity(payload.getQuantity());
        return itemRepo.save(existing);
    }
}
