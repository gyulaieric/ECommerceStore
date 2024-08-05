package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.repository.CartRepository;
import com.gyulaieric.ECommerceStore.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CartRepository cartRepository;

    public ProductService(ProductRepository productRepository, CartRepository cartRepository) {
        this.productRepository = productRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProduct(Long productId) {
        return productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException(String.format("Product with id %s doesn't exist", productId))
        );
    }

    @Override
    public void addProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public void updateProduct(Long id, Product product) {
        Product productToUpdate = productRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Product with id %s doesn't exist", id))
        );

        productToUpdate.setName(product.getName());
        productToUpdate.setImageUrl(product.getImageUrl());
        productToUpdate.setPrice(product.getPrice());
        productToUpdate.setOnSale(product.isOnSale());
        productToUpdate.setOldPrice(product.getOldPrice());
        productToUpdate.setCategory(product.getCategory());

        productRepository.save(productToUpdate);
    }

    @Override
    public void deleteProduct(Long id) {
        if(!productRepository.existsById(id)) {
            throw new IllegalStateException(String.format("Product with id %s doesn't exist", id));
        }

        List<Cart> cartList = cartRepository.findAllByProductId(id);
        for(Cart cart : cartList) {
            cartRepository.deleteById(cart.getId());
        }

        productRepository.deleteById(id);
    }
}
