package com.gyulaieric.ECommerceStore.controller;

import com.gyulaieric.ECommerceStore.model.Category;
import com.gyulaieric.ECommerceStore.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }
}
