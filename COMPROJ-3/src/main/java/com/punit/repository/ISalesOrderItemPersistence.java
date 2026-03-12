package com.punit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.punit.entities.SalesOrderItem;


public interface ISalesOrderItemPersistence extends JpaRepository<SalesOrderItem, Integer> {
			
	
}