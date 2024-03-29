package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.model.User;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ICartService {
    List<Cart> getCart(Long userId);
    void addToCart(Authentication authentication, Long productId, int quantity);
    void updateCart(Long id, int quantity);
    void deleteFromCart(Long id);
}
