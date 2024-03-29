package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getCategories();
    void addCategory(Category category);
    void updateCategory(int id, Category category);
    void deleteCategory(int id);
}
