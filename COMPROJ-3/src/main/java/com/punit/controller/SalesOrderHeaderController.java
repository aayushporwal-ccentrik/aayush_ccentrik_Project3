package com.punit.controller;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.punit.entities.SalesOrderHeader;
import com.punit.service.SalesOrderHeaderService;

@RestController
public class SalesOrderHeaderController {

    @Autowired
    SalesOrderHeaderService headerService;

    // GET ALL SALES ORDERS
    @GetMapping("/salesorderheader")
    public List<SalesOrderHeader> getAllOrders() {
        return headerService.getAllOrders();
    }

    // GET SALES ORDER BY ID
    @GetMapping("/salesorderheader/{orderId}")
    public SalesOrderHeader getOrderById(@PathVariable("orderId") Long id) {
        Optional<SalesOrderHeader> result = headerService.getOrderById(id);
        if (!result.isPresent()) {
            return new SalesOrderHeader();  // return empty object if not found
        }
        return result.get();
    }

    // CREATE SALES ORDER
    @PostMapping("/salesorderheader")
    public SalesOrderHeader createOrder(@RequestBody SalesOrderHeader header) {
        return headerService.createOrder(header);
    }

    // UPDATE SALES ORDER
    @PutMapping("/salesorderheader")
    public SalesOrderHeader updateOrder(@RequestBody SalesOrderHeader header) {
        return headerService.updateOrder(header);
    }

	/*
	 * // DELETE SALES ORDER
	 * 
	 * @DeleteMapping("/salesorderheader/{orderId}") public String
	 * deleteOrder(@PathVariable("orderId") Long id) { return
	 * headerService.deleteOrder(id); }
	 */
}