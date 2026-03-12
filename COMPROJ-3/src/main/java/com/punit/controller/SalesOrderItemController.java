package com.punit.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.punit.entities.SalesOrderItem;
import com.punit.service.SalesOrderItemService;

@RestController
public class SalesOrderItemController {

    @Autowired
    SalesOrderItemService itemService;

    // GET ALL ITEMS
    @GetMapping("/salesorderitem")
    public List<SalesOrderItem> getAllItems() {
        return itemService.getAllItems();
    }

    // GET ITEM BY ITEM NUMBER
    // ✅ CHANGED: path var {itemNumber}, type Integer
    @GetMapping("/salesorderitem/{itemNumber}")
    public SalesOrderItem getItemByItemNumber(@PathVariable("itemNumber") Integer itemNumber) {
        Optional<SalesOrderItem> result = itemService.getItemByItemNumber(itemNumber);
        if (!result.isPresent()) {
            return new SalesOrderItem();  // return empty object if not found
        }
        return result.get();
    }

    // CREATE ITEM
    @PostMapping("/salesorderitem")
    public SalesOrderItem createItem(@RequestBody SalesOrderItem item) {
        return itemService.createItem(item);
    }

    // UPDATE ITEM
    // ✅ CHANGED: path var {itemNumber}, type Integer
    @PutMapping("/salesorderitem/{itemNumber}")
    public SalesOrderItem updateItem(
            @PathVariable("itemNumber") Integer itemNumber,
            @RequestBody SalesOrderItem item) {

        return itemService.updateItem(itemNumber, item);
    }

    /*
     * // DELETE ITEM
     *
     * @DeleteMapping("/salesorderitem/{itemNumber}") public String
     * deleteItem(@PathVariable("itemNumber") Integer itemNumber) { return
     * itemService.deleteItem(itemNumber); }
     */
}