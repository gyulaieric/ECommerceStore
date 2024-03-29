package com.gyulaieric.ECommerceStore.controller;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.service.CartService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("http://127.0.0.1:5500")
@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{userId}")
    public List<Cart> getCart(@PathVariable Long userId) {
        return cartService.getCart(userId);
    }

    @PostMapping("/add/{productId}")
    public void addToCart(Authentication authentication, @PathVariable Long productId, @RequestBody int quantity) {
        cartService.addToCart(authentication, productId, quantity);
    }

    @PutMapping("/update/{cartId}")
    public void updateCart(@PathVariable Long cartId, @RequestBody int quantity) {
        cartService.updateCart(cartId, quantity);
    }

    @DeleteMapping("/delete/{cartId}")
    public void deleteCart(@PathVariable Long cartId) {
        cartService.deleteFromCart(cartId);
    }
}
