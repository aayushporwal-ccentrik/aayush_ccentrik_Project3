//Devloped by PUNIT 06-03-2026
package com.punit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.punit.entities.Vendor;
import com.punit.repository.IVendorPersistence;

@Service
public class VendorService {

	@Autowired
	IVendorPersistence venInterface;
	
	//Read Vendor Data
	public List<Vendor> readVendor(){
		return venInterface.findAll();
	}
	
	//Read vendor by ID this will be used to edit the data based on vendorId
	public Optional<Vendor> getVendorById(Long id) {
	    return venInterface.findById(id);
	}
	
	//Create Vendor Data
	public Vendor createVendor(Vendor payload) {
		return venInterface.save(payload);
	}
	
	//Update Vendor Data
	public Vendor updateVendor(Vendor payload) {
		return venInterface.save(payload);
	}
	
	//Delete Vendor Data
	public String deleteVendor(Long id) {
		venInterface.deleteById(id);
		return "Delete Vendor successfully";
	}
}
