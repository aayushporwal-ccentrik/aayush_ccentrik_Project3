//Devloped by PUNIT 06-03-2026
package com.punit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punit.entities.Vendor;

public interface IVendorPersistence extends JpaRepository<Vendor, Long> {

}
