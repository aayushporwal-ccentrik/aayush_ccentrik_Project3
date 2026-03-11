package com.punit.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SALESORDER_ITEM")
public class SalesOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Long soItemId;
    
    @Column(name = "ITEM_NUMBER", nullable = false)
    private Integer itemNumber;

    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "UOM")
    private String uom;

    @OneToOne
    @JoinColumn(name = "SO_NUMBER", nullable = false)
    private SalesOrderHeader salesOrderHeader;

    public SalesOrderItem() {
    }

    public SalesOrderItem(Long soItemId, Integer itemNumber,
                           String material, Integer quantity, String uom,
                           SalesOrderHeader salesOrderHeader) {
        this.soItemId = soItemId;
        this.itemNumber = itemNumber;
        this.material = material;
        this.quantity = quantity;
        this.uom = uom;
        this.salesOrderHeader = salesOrderHeader;
    }

    public Long getSoItemId() {
        return soItemId;
    }

    public void setSoItemId(Long soItemId) {
        this.soItemId = soItemId;
    }

    public Integer getItemNumber() {
        return itemNumber;
    }

    public void setItemNumber(Integer itemNumber) {
        this.itemNumber = itemNumber;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public SalesOrderHeader getSalesOrderHeader() {
        return salesOrderHeader;
    }

    public void setSalesOrder(SalesOrderHeader salesOrderHeader) {
        this.salesOrderHeader = salesOrderHeader;
    }
}