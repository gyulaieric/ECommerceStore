package com.gyulaieric.ECommerceStore.controller;

import com.gyulaieric.ECommerceStore.model.*;
import com.gyulaieric.ECommerceStore.service.CategoryService;
import com.gyulaieric.ECommerceStore.service.OrderService;
import com.gyulaieric.ECommerceStore.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/admin/api")
public class AdminController {
    private final CategoryService categoryService;
    private final ProductService productService;
    private final OrderService orderService;

    public AdminController(CategoryService categoryService, ProductService productService, OrderService orderService) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.orderService = orderService;
    }

    @PostMapping("/category/add")
    public void addCategory(@Valid @RequestBody Category category) {
        categoryService.addCategory(category);
    }

    @PutMapping("/category/update/{categoryId}")
    public void updateCategory(@PathVariable int categoryId, @Valid @RequestBody Category category) {
        categoryService.updateCategory(categoryId, category);
    }

    @DeleteMapping("/category/delete/{categoryId}")
    public void deleteCategory(@PathVariable int categoryId) {
        categoryService.deleteCategory(categoryId);
    }

    @PostMapping("/product/add")
    public void addProduct(@Valid @RequestBody Product product) {
        productService.addProduct(product);
    }

    @PutMapping("/product/update/{productId}")
    public void updateProduct(@PathVariable Long productId, @Valid @RequestBody Product product) {
        productService.updateProduct(productId, product);
    }

    @DeleteMapping("/product/delete/{productId}")
    public void deleteProduct(@PathVariable Long productId) {
        productService.deleteProduct(productId);
    }

    @GetMapping("/orders/")
    public List<Order> getOrders() {
        return orderService.getOrders();
    }

    @PostMapping("/orders/update/{id}")
    public void updateOrder(@PathVariable Long id, @Valid @RequestBody Order order) {
        orderService.updateOrder(id, order);
    }

    @DeleteMapping("/orders/delete/{id}")
    public void deleteOrder(@PathVariable Long id) {
        orderService.deleteOrder(id);
    }

    @PostMapping("/orders/addProduct")
    public void addProductToOrder(@Valid @RequestBody OrderedItemDTO orderedItemDTO) {
        orderService.addProductToOrder(orderedItemDTO.getOrderId(), orderedItemDTO.getOrderedItemId(), orderedItemDTO.getProductId(), orderedItemDTO.getQuantity());
    }

    @DeleteMapping("/orders/removeProduct")
    public void removeProductFromOrder(@Valid @RequestBody OrderedItemDTO orderedItemDTO) {
        orderService.deleteProductFromOrder(orderedItemDTO.getOrderId(), orderedItemDTO.getOrderedItemId());
    }
}
