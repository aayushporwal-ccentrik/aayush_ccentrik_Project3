package com.punit.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "PO_NUMBER")
public class Po_Number {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PO_ID", nullable = false)
    private Long poId;

    @Column(name = "PO_NUMBER")
    private String poNumber;

    @Column(name = "ORDER_DATE")
    private LocalDate order_date;

    @Column(name = "DELIVERY_DATE")
    private LocalDate delivery_date;

    @ManyToOne
    @JoinColumn(name = "VENDOR_ID", nullable = false)
    private Vendor vendor;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL)
    private List<Po_Number_Item> items;

    public Po_Number() {
    }

    public Po_Number(Long poId, String poNumber, LocalDate order_date, LocalDate delivery_date, Vendor vendor) {
        this.poId = poId;
        this.poNumber = poNumber;
        this.order_date = order_date;
        this.delivery_date = delivery_date;
        this.vendor = vendor;
    }

    public Long getPoId() {
        return poId;
    }

    public void setPoId(Long poId) {
        this.poId = poId;
    }

    public String getPoNumber() {
        return poNumber;
    }

    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    public LocalDate getOrder_date() {
        return order_date;
    }

    public void setOrder_date(LocalDate order_date) {
        this.order_date = order_date;
    }

    public LocalDate getDelivery_date() {
        return delivery_date;
    }

    public void setDelivery_date(LocalDate delivery_date) {
        this.delivery_date = delivery_date;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }

    public List<Po_Number_Item> getItems() {
        return items;
    }

    public void setItems(List<Po_Number_Item> items) {
        this.items = items;
    }
}