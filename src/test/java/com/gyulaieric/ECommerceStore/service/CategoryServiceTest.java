package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Category;
import com.gyulaieric.ECommerceStore.repository.CategoryRepository;
import org.checkerframework.checker.nullness.Opt;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    public void getCategories() {
        Category category1 = new Category();
        Category category2 = new Category();
        List<Category> categories = List.of(category1, category2);

        Mockito.when(categoryRepository.findAll()).thenReturn(categories);

        List<Category> result = categoryService.getCategories();
        assertEquals(categories, result);
    }

    @Test
    public void addCategory() {
        Category category = new Category();

        categoryService.addCategory(category);

        Mockito.verify(categoryRepository).save(category);
    }

    @Test
    public void updateCategory() {
        int categoryId = 1;
        Category categoryToUpdate = new Category();
        categoryToUpdate.setId(categoryId);

        Category newCategory = new Category();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(categoryToUpdate));

        categoryService.updateCategory(categoryId, newCategory);

        Mockito.verify(categoryRepository).save(Mockito.any(Category.class));
    }

    @Test
    public void updateCategory_NonExistentCategory_ShouldThrowIllegalStateException() {
        int categoryId = 1;
        Category newCategory = new Category();

        Mockito.when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> categoryService.updateCategory(categoryId, newCategory));
        assertEquals("Category with id " + categoryId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void deleteCategory() {
        int categoryId = 1;

        Mockito.when(categoryRepository.existsById(categoryId)).thenReturn(true);

        categoryService.deleteCategory(categoryId);
        Mockito.verify(categoryRepository).deleteById(categoryId);
    }

    @Test
    public void deleteCategory_NonExistentCategory_ShouldThrowIllegalStateException() {
        int categoryId = 1;

        Mockito.when(categoryRepository.existsById(categoryId)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> categoryService.deleteCategory(categoryId));
        assertEquals("Category with id " + categoryId + " doesn't exist", exception.getMessage());
    }
}
