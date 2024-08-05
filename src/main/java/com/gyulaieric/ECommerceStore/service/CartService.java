package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.model.User;
import com.gyulaieric.ECommerceStore.repository.CartRepository;
import com.gyulaieric.ECommerceStore.repository.ProductRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CartService implements ICartService{

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<Cart> getCart(Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new IllegalStateException(String.format("User %s doesn't exist", authentication.getName()))
        );

        return cartRepository.findAllByUser(user);
    }

    @Override
    public void addToCart(Authentication authentication, Long productId, Integer quantity) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new IllegalStateException(String.format("User %s doesn't exist", authentication.getName()))
        );

        Product product = productRepository.findById(productId).orElseThrow(
                () -> new IllegalStateException(String.format("Product with id %s doesn't exist", productId))
        );

        Optional<Cart> existingCart = cartRepository.findAllByUser(user).stream().filter(item -> item.getProduct().equals(product)).findFirst();

        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();

            cart.setQuantity(cart.getQuantity() + quantity);

            cartRepository.save(cart);
        } else {
            cartRepository.save(new Cart(user, product, quantity));
        }
    }

    @Override
    public void updateCart(Authentication authentication, Long id, Integer quantity) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new IllegalStateException(String.format("User %s doesn't exist", authentication.getName()))
        );

        Cart cartToUpdate = cartRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Cart item with id %s doesn't exist", id))
        );

        if (cartToUpdate.getUser().equals(user)) {
            cartToUpdate.setQuantity(quantity);
            cartRepository.save(cartToUpdate);
        } else {
            throw new IllegalStateException("You can only update your own cart");
        }
    }

    @Override
    public void deleteFromCart(Authentication authentication, Long id) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow(
                () -> new IllegalStateException(String.format("User %s doesn't exist", authentication.getName()))
        );

        Cart cartToDelete = cartRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Cart item with id %s doesn't exist", id))
        );

        if (cartToDelete.getUser().equals(user)) {
            cartRepository.delete(cartToDelete);
        } else {
            throw new IllegalStateException("You can only delete items from your own cart");
        }
    }
}
