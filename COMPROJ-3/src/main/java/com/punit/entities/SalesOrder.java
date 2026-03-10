package com.punit.entities;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "SALESORDER")
public class SalesOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SO_ID", nullable = false)
    private Long soId;

    @Column(name = "SALES_ORDER_NUMBER")
    private String salesOrderNumber;

    @Column(name = "ORDER_DATE")
    private LocalDate dateOfOrder;

    @Column(name = "DELIVERY_DATE")
    private LocalDate dateOfDelivery;

    @ManyToOne
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "salesOrder", cascade = CascadeType.ALL)
    private List<SalesOrder_Item> items;

    public SalesOrder() {
    }

    public SalesOrder(Long soId, String salesOrderNumber, LocalDate dateOfOrder,
                      LocalDate dateOfDelivery, Customer customer) {
        this.soId = soId;
        this.salesOrderNumber = salesOrderNumber;
        this.dateOfOrder = dateOfOrder;
        this.dateOfDelivery = dateOfDelivery;
        this.customer = customer;
    }

    public Long getSoId() {
        return soId;
    }

    public void setSoId(Long soId) {
        this.soId = soId;
    }

    public String getSalesOrderNumber() {
        return salesOrderNumber;
    }

    public void setSalesOrderNumber(String salesOrderNumber) {
        this.salesOrderNumber = salesOrderNumber;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
    }

    public LocalDate getDateOfDelivery() {
        return dateOfDelivery;
    }

    public void setDateOfDelivery(LocalDate dateOfDelivery) {
        this.dateOfDelivery = dateOfDelivery;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<SalesOrder_Item> getItems() {
        return items;
    }

    public void setItems(List<SalesOrder_Item> items) {
        this.items = items;
    }
}