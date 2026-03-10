package com.punit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punit.entities.Address;

public interface IAddressPersistence extends JpaRepository<Address, Long>{

}
