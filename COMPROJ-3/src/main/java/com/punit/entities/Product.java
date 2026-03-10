package com.punit.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "PRODUCT")
public class Product {

	@Id
	@Column(nullable = false , name="PRODUCT_ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long productId;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "TYPE")
	private String type;
	
	@Column(name = "SECTOR")
	private String sector;
	
	@Column(name = "PRODUCT_GROUP")
	private String group;
	
	@Column(name = "UOM")
	private String uom;
	
	
	public Product() {
		super();
	}
	public Product(Long productId, String name, String type, String sector, String group, String uom) {
		super();
		this.productId = productId;
		this.name = name;
		this.type = type;
		this.sector = sector;
		this.group = group;
		this.uom = uom;
	}
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSector() {
		return sector;
	}
	public void setSector(String sector) {
		this.sector = sector;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	
	
	
}
