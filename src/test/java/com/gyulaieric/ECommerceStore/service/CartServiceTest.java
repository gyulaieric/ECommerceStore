package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.model.User;
import com.gyulaieric.ECommerceStore.repository.CartRepository;
import com.gyulaieric.ECommerceStore.repository.ProductRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    private CartService cartService;

    private Authentication authentication;
    private static User user;

    @BeforeEach
    public void setUp() {
        cartService = new CartService(cartRepository, userRepository, productRepository);

        user = new User(1L, "test@example.com", "testuser", "testpassword", new HashSet<>());

        authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getName()).thenReturn(user.getUsername());
    }

    @Test
    public void getCart_ShouldReturnCart() {
        Long userId = 1L;
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        List<Cart> expectedCart = Collections.singletonList(new Cart(user, new Product(), 1));
        Mockito.when(cartRepository.findAllByUser(user)).thenReturn(expectedCart);

        List<Cart> result = cartService.getCart(authentication);
        assertEquals(expectedCart, result);
    }

    @Test
    public void getCart_NonExistentUser_ShouldThrowIllegalStateException() {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        assertThrows(IllegalStateException.class, () -> cartService.getCart(authentication));
    }

    @Test
    public void addToCart_ItemNotInCart_ShouldSaveCart() {
        Long productId = 1L;
        Integer quantity = 2;
        Product product = new Product();
        product.setId(productId);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(cartRepository.findAllByUser(Mockito.any())).thenReturn(Collections.emptyList());

        cartService.addToCart(authentication, productId, quantity);

        Mockito.verify(cartRepository).save(Mockito.any(Cart.class));
    }

    @Test
    public void addToCart_ItemAlreadyInCart_ShouldIncreaseQuantity() {
        Long productId = 1L;
        Integer quantity = 2;
        Product product = new Product();
        product.setId(productId);

        Long cartId = 1L;
        Cart cartToUpdate = new Cart();
        cartToUpdate.setId(cartId);
        cartToUpdate.setUser(user);
        cartToUpdate.setProduct(product);
        cartToUpdate.setQuantity(1);

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        Mockito.when(cartRepository.findAllByUser(Mockito.any())).thenReturn(List.of(cartToUpdate));

        cartService.addToCart(authentication, productId, quantity);

        Mockito.verify(cartRepository).save(Mockito.any(Cart.class));
    }

    @Test
    public void addToCart_NonExistentUser_ShouldThrowIllegalStateException() {
        Long productId = 1L;
        Integer quantity = 2;

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.addToCart(authentication, productId, quantity));
        assertEquals("User " + authentication.getName() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void addToCart_NonExistentProduct_ShouldThrowIllegalStateException() {
        Long productId = 1L;
        Integer quantity = 2;

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.addToCart(authentication, productId, quantity));
        assertEquals("Product with id " + productId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void updateCart_ShouldUpdateCart() {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Long cartId = 1L;
        Integer newQuantity = 5;
        Cart cartToUpdate = new Cart();
        cartToUpdate.setId(cartId);
        cartToUpdate.setUser(user);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartToUpdate));

        cartService.updateCart(authentication, cartId, newQuantity);
        Mockito.verify(cartRepository).save(cartToUpdate);
    }

    @Test
    public void updateCart_NonExistentUser_ShouldThrowIllegalStateException() {
        Long cartId = 1L;
        Integer newQuantity = 5;

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.updateCart(authentication, cartId ,newQuantity));
        assertEquals("User " + authentication.getName() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void updateCart_NonExistentCart_ShouldThrowIllegalStateException() {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Long cartId = 1L;
        Integer newQuantity = 5;

        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.updateCart(authentication, cartId ,newQuantity));
        assertEquals("Cart item with id " + cartId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void updateCart_UserIsNotOwnerOfCart_ShouldThrowIllegalStateException() {
        User wrongUser = new User();
        wrongUser.setUsername("notOwnerOfCart");

        Long cartId = 1L;
        Integer newQuantity = 5;

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(wrongUser));

        Cart cartToUpdate = new Cart();
        cartToUpdate.setId(cartId);
        cartToUpdate.setUser(user);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartToUpdate));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.updateCart(authentication, cartId, newQuantity));
        assertEquals("You can only update your own cart", exception.getMessage());
    }

    @Test
    public void deleteFromCart_ShouldDeleteCart() {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Long cartId = 1L;
        Cart cartToDelete = new Cart();
        cartToDelete.setId(cartId);
        cartToDelete.setUser(user);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartToDelete));

        cartService.deleteFromCart(authentication, cartId);

        Mockito.verify(cartRepository).delete(cartToDelete);
    }

    @Test
    public void testDeleteFromCart_NonExistingUser_ShouldThrowIllegalStateException() {
        Long cartId = 1L;

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.deleteFromCart(authentication, cartId));
        assertEquals("User " + authentication.getName() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void testDeleteFromCart_NonExistingCart_ShouldThrowIllegalStateException() {
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Long cartId = 1L;
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.deleteFromCart(authentication, cartId));
        assertEquals("Cart item with id " + cartId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void deleteFromCart_UserIsNotOwnerOfCart_ShouldThrowIllegalStateException() {
        User wrongUser = new User();
        wrongUser.setUsername("notOwnerOfCart");

        Long cartId = 1L;
        Integer newQuantity = 5;

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(wrongUser));

        Cart cartToUpdate = new Cart();
        cartToUpdate.setId(cartId);
        cartToUpdate.setUser(user);
        Mockito.when(cartRepository.findById(cartId)).thenReturn(Optional.of(cartToUpdate));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> cartService.deleteFromCart(authentication, cartId));
        assertEquals("You can only delete items from your own cart", exception.getMessage());
    }
}
