package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Cart;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface ICartService {
    List<Cart> getCart(Authentication authentication);
    void addToCart(Authentication authentication, Long productId, Integer quantity);
    void updateCart(Authentication authentication, Long id, Integer quantity);
    void deleteFromCart(Authentication authentication, Long id);
}
