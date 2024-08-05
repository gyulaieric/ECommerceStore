package com.gyulaieric.ECommerceStore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @SequenceGenerator(
            name = "product_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "product_sequence",
            strategy = GenerationType.SEQUENCE
    )
    private Long id;

    @NotEmpty(message = "Name should not be empty")
    private String name;
    private String description;
    @NotEmpty(message = "Image URL should not be empty")
    private String imageUrl;
    @NotNull(message = "Price should not be null")
    private Double price;
    private Double oldPrice;
    @NotNull(message = "quantity should not be null")
    @Min(1)
    private int quantity;
    @NotNull(message = "onSale should not be null")
    private Boolean onSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public Product() { };

    public Product(String name, String description, String imageUrl, Double price, Double oldPrice , Boolean onSale , int quantity, Category category) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.oldPrice = oldPrice;
        this.onSale = onSale;
        this.quantity = quantity;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(Double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public Boolean isOnSale() {
        return onSale;
    }

    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return quantity == product.quantity && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(description, product.description) && Objects.equals(imageUrl, product.imageUrl) && Objects.equals(price, product.price) && Objects.equals(oldPrice, product.oldPrice) && Objects.equals(onSale, product.onSale) && Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, imageUrl, price, oldPrice, quantity, onSale, category);
    }
}
