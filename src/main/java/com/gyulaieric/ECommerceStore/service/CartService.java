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
    public List<Cart> getCart(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new IllegalStateException(String.format("User with id %s doesn't exist", userId))
        );

        return cartRepository.findAllByUser(user);
    }

    @Override
    public void addToCart(Authentication authentication, Long productId, int quantity) {
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
    public void updateCart(Long id, int quantity) {
        Cart cartToUpdate = cartRepository.findById(id).orElseThrow(
                () -> new IllegalStateException(String.format("Cart item with id %s doesn't exist", id))
        );

        cartToUpdate.setQuantity(quantity);

        cartRepository.save(cartToUpdate);
    }

    @Override
    public void deleteFromCart(Long id) {
        if(!cartRepository.existsById(id)) {
            throw new IllegalStateException(String.format("Cart item with id %s doesn't exist", id));
        }

        cartRepository.deleteById(id);
    }
}
