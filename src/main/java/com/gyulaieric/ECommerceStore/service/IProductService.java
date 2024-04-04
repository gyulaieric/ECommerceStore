package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Product;

import java.util.List;

public interface IProductService {
    List<Product> getProducts();
    Product getProduct(Long productId);
    void addProduct(Product product);
    void updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
