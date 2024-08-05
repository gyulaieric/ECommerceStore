package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.repository.CartRepository;
import com.gyulaieric.ECommerceStore.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    public void getProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Product 1", "description", "image1.jpg", 10.0, null, false, 1, null));
        productList.add(new Product("Product 2", "description", "image2.jpg", 15.0, null, false, 1, null));

        Mockito.when(productRepository.findAll()).thenReturn(productList);

        List<Product> result = productService.getProducts();

        assertEquals(productList.size(), result.size());
        assertEquals(productList, result);
    }

    @Test
    public void getProduct() {
        Long productId = 1L;

        Product product = new Product("Product", "description", "image.jpg", 10.0, null, false, 1, null);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Product result = productService.getProduct(productId);

        assertEquals(product, result);
    }

    @Test
    public void getProducts_NonExistentProduct_ShouldThrowIllegalStateException() {
        Long productId = 1L;

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> productService.getProduct(productId));
        assertEquals("Product with id " + productId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void testAddProduct() {
        Product product = new Product("Product 1", "description", "image1.jpg", 10.0, null, false, 1, null);

        productService.addProduct(product);

        Mockito.verify(productRepository).save(product);
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product existingProduct = new Product("Product", "description", "image.jpg", 10.0, null, false, 1, null);
        Product updatedProduct = new Product("Updated Product", "updated description", "updated.jpg", 15.0, 10.99, true, 2, null);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));

        productService.updateProduct(productId, updatedProduct);

        Mockito.verify(productRepository, Mockito.times(1)).save(existingProduct);
        assertEquals(updatedProduct.getName(), existingProduct.getName());
        assertEquals(updatedProduct.getImageUrl(), existingProduct.getImageUrl());
        assertEquals(updatedProduct.getPrice(), existingProduct.getPrice());
        assertEquals(updatedProduct.isOnSale(), existingProduct.isOnSale());
        assertEquals(updatedProduct.getOldPrice(), existingProduct.getOldPrice());
        assertEquals(updatedProduct.getCategory(), existingProduct.getCategory());
    }

    @Test
    public void updateProduct_NonExistentProduct_ShouldThrowIllegalStateException() {
        Long productId = 1L;
        Product product = new Product("Updated Product", "updated description", "updated.jpg", 15.0, 10.99, true, 2, null);
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> productService.updateProduct(productId, product));
        assertEquals("Product with id " + productId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void deleteProduct() {
        Long productId = 1L;
        Product product = new Product("Product", "description", "image.jpg", 10.0, null, false, 1, null);

        Cart cart = new Cart();
        cart.setId(1L);
        cart.setProduct(product);

        Mockito.when(productRepository.existsById(productId)).thenReturn(true);
        Mockito.when(cartRepository.findAllByProductId(productId)).thenReturn(List.of(cart));

        productService.deleteProduct(productId);

        Mockito.verify(cartRepository, Mockito.times(1)).findAllByProductId(productId);
        Mockito.verify(cartRepository).deleteById(Mockito.anyLong());
        Mockito.verify(productRepository, Mockito.times(1)).deleteById(productId);
    }

    @Test
    public void deleteProduct_NonExistentProduct_ShouldThrowIllegalStateException() {
        Long productId = 1L;

        Mockito.when(productRepository.existsById(productId)).thenReturn(false);

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> productService.deleteProduct(productId));
        assertEquals("Product with id " + productId + " doesn't exist", exception.getMessage());
    }
}
