package com.punit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.punit.entities.SalesOrderHeader;
	
	public interface ISalesOrderHeaderPersistence extends JpaRepository<SalesOrderHeader, Long> {
				

}