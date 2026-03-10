package com.punit.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "SALESORDER_ITEM")
public class SalesOrder_Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SO_ITEM_ID", nullable = false)
    private Long soItemId;

    @Column(name = "ITEM_NUMBER")
    private Integer itemNumber;

    @Column(name = "MATERIAL")
    private String material;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "UOM")
    private String uom;

    @ManyToOne
    @JoinColumn(name = "SO_ID", nullable = false)
    private SalesOrder salesOrder;

    public SalesOrder_Item() {
    }

    public SalesOrder_Item(Long soItemId, Integer itemNumber,
                           String material, Integer quantity, String uom,
                           SalesOrder salesOrder) {
        this.soItemId = soItemId;
        this.itemNumber = itemNumber;
        this.material = material;
        this.quantity = quantity;
        this.uom = uom;
        this.salesOrder = salesOrder;
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

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
    }
}