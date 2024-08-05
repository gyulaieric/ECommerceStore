package com.gyulaieric.ECommerceStore.repository;

import com.gyulaieric.ECommerceStore.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {
}
