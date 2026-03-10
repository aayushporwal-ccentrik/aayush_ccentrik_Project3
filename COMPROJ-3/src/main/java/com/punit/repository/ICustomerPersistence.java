package com.punit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punit.entities.Customer;

public interface ICustomerPersistence extends JpaRepository<Customer, Long>{

}
