package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    List<Product> getProducts();
    Optional<Product> getProduct(Long productId);
    void addProduct(Product product);
    void updateProduct(Long id, Product product);
    void deleteProduct(Long id);
}
