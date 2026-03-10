package com.punit.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "PO_NUMBER_ITEM")
public class Po_Number_Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PO_ITEM_ID", nullable = false)
    private Long poItemId;

    @Column(name = "ITEM_NO")
    private Integer itemNo;

    @Column(name = "MATERIAL_NO")
    private String materialNo;

    @Column(name = "QUANTITY")
    private Integer quantity;

    @Column(name = "UOM")
    private String uom;

    @ManyToOne
    @JoinColumn(name = "PO_ID", nullable = false)
    private Po_Number purchaseOrder;

    public Po_Number_Item() {
    }

    public Po_Number_Item(Long poItemId, Integer itemNo, String materialNo, Integer quantity, String uom, Po_Number purchaseOrder) {
        this.poItemId = poItemId;
        this.itemNo = itemNo;
        this.materialNo = materialNo;
        this.quantity = quantity;
        this.uom = uom;
        this.purchaseOrder = purchaseOrder;
    }

    public Long getPoItemId() {
        return poItemId;
    }

    public void setPoItemId(Long poItemId) {
        this.poItemId = poItemId;
    }

    public Integer getItemNo() {
        return itemNo;
    }

    public void setItemNo(Integer itemNo) {
        this.itemNo = itemNo;
    }

    public String getMaterialNo() {
        return materialNo;
    }

    public void setMaterialNo(String materialNo) {
        this.materialNo = materialNo;
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

    public Po_Number getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(Po_Number purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }
}