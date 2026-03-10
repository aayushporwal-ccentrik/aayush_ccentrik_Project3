
package com.punit.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.punit.entities.Vendor;
import com.punit.service.VendorService;

@RestController
@RequestMapping("/vendors") // Good practice to group routes
public class VendorController {

    @Autowired
    private VendorService venService;

    //Read all vendor data
    @GetMapping
    public List<Vendor> getAllVendors() {
        return venService.readVendor();
    }

    //READ BY ID - Get vendor by ID
    @GetMapping("/{id}")
    public ResponseEntity<Vendor> getVendorById(@PathVariable Long id) {
        Optional<Vendor> vendor = venService.getVendorById(id);
        if(vendor.isPresent()) {
            return ResponseEntity.ok(vendor.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Create vendor
    @PostMapping
    public Vendor createVendor(@RequestBody Vendor payload) {
        return venService.createVendor(payload);
    }

    //Update vendor by ID
    @PutMapping("/{id}")
    public ResponseEntity<Vendor> updateVendor(@PathVariable Long id, @RequestBody Vendor payload) {
        Optional<Vendor> existingVendor = venService.getVendorById(id);
        if(existingVendor.isPresent()) {
            payload.setVendorId(id);
            Vendor updatedVendor = venService.updateVendor(payload);
            return ResponseEntity.ok(updatedVendor);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    //Delete vendor by ID
    @DeleteMapping("/{id}")
    public String deleteVendor(@PathVariable Long id) {
        return venService.deleteVendor(id);
    }
}
