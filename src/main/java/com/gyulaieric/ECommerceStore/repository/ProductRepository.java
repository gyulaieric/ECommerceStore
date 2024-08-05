package com.gyulaieric.ECommerceStore.repository;

import com.gyulaieric.ECommerceStore.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
