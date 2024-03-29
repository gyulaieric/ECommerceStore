package com.gyulaieric.ECommerceStore.model;

import jakarta.persistence.*;

@Entity
@Table(name = "ordered_items")
public class OrderedItem {
    @Id
    @SequenceGenerator(
            name = "ordered_items_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "ordered_items_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;

    public OrderedItem() { }

    public OrderedItem( Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
