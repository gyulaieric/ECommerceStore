package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Category;
import com.gyulaieric.ECommerceStore.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public void addCategory(Category category) {
        categoryRepository.save(category);
    }

    @Override
    public void updateCategory(int id, Category category) {
        Category categoryToUpdate = categoryRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Category with id %s doesn't exist", id)));

        categoryToUpdate.setName(category.getName());
        categoryToUpdate.setImageUrl(category.getImageUrl());
        categoryToUpdate.setDescription(category.getDescription());

        categoryRepository.save(categoryToUpdate);
    }

    @Override
    public void deleteCategory(int id) {
        if(!categoryRepository.existsById(id)) {
            throw new IllegalStateException(String.format("Category with id %s doesn't exist", id));
        }

        categoryRepository.deleteById(id);
    }
}
