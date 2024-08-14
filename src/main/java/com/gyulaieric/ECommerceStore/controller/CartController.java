package com.gyulaieric.ECommerceStore.controller;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.service.CartService;
import jakarta.validation.Valid;
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

    @GetMapping("/")
    public List<Cart> getCart(Authentication authentication) {
        return cartService.getCart(authentication);
    }

    @PostMapping("/{productId}")
    public void addToCart(Authentication authentication, @PathVariable Long productId, @Valid @RequestBody int quantity) {
        cartService.addToCart(authentication, productId, quantity);
    }

    @PutMapping("/{cartId}")
    public void updateCart(Authentication authentication, @PathVariable Long cartId, @Valid @RequestBody Integer quantity) {
        cartService.updateCart(authentication, cartId, quantity);
    }

    @DeleteMapping("/{cartId}")
    public void deleteCart(Authentication authentication, @PathVariable Long cartId) {
        cartService.deleteFromCart(authentication, cartId);
    }
}
