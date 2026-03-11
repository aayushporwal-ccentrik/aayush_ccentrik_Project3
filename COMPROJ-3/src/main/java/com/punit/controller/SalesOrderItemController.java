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

    // GET ITEM BY ID
    @GetMapping("/salesorderitem/{itemId}")
    public SalesOrderItem getItemById(@PathVariable("itemId") Long id) {
        Optional<SalesOrderItem> result = itemService.getItemById(id);
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
    @PutMapping("/salesorderitem/{itemId}")
    public SalesOrderItem updateItem(
            @PathVariable("itemId") Long itemId,
            @RequestBody SalesOrderItem item) {

        return itemService.updateItem(itemId, item);
    }

	/*
	 * // DELETE ITEM
	 * 
	 * @DeleteMapping("/salesorderitem/{itemId}") public String
	 * deleteItem(@PathVariable("itemId") Long id) { return
	 * itemService.deleteItem(id); }
	 */
}