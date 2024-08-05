package com.gyulaieric.ECommerceStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.model.Role;
import com.gyulaieric.ECommerceStore.model.User;
import com.gyulaieric.ECommerceStore.repository.RoleRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import com.gyulaieric.ECommerceStore.service.CartService;
import com.nimbusds.jose.proc.SecurityContext;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(CartController.class)
public class CartControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CartService cartService;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private RoleRepository roleRepository;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private User user;
    private Authentication authentication;

    @Test
    public void getCart() throws Exception {
        User user = new User(1L, "user@email.com", "user", "password", Set.of(new Role("USER")));

        Cart cart1 = new Cart(user, new Product(), 1);
        cart1.setId(1L);
        Cart cart2 = new Cart(user, new Product(), 2);
        cart2.setId(2L);

        Mockito.when(cartService.getCart(Mockito.any(Authentication.class))).thenReturn(List.of(cart1, cart2));

        mockMvc.perform(get("/api/cart/")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", CoreMatchers.is(1)))
                .andExpect(jsonPath("$.[0].quantity", CoreMatchers.is(cart1.getQuantity())))
                .andExpect(jsonPath("$.[1].id", CoreMatchers.is(2)))
                .andExpect(jsonPath("$.[1].quantity", CoreMatchers.is(cart2.getQuantity())));

        Mockito.verify(cartService).getCart(Mockito.any(Authentication.class));
    }

    @Test
    public void addToCart() throws Exception {
        Long productId = 1L;
        int quantity = 2;

        mockMvc.perform(post("/api/cart/add/" + productId)
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(quantity)))
                .andExpect(status().isOk());

        Mockito.verify(cartService).addToCart(Mockito.any(Authentication.class), Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void updateCart() throws Exception {
        Long cartId = 1L;
        int quantity = 1;
        Cart cart = new Cart();

        mockMvc.perform(put("/api/cart/update/" + cartId)
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quantity)))
                .andExpect(status().isOk());

        Mockito.verify(cartService).updateCart(Mockito.any(Authentication.class), Mockito.anyLong(), Mockito.anyInt());
    }

    @Test
    public void deleteCart() throws Exception {
        Long cartId = 1L;

        mockMvc.perform(delete("/api/cart/delete/" + cartId)
                        .with(jwt()))
                .andExpect(status().isOk());

        Mockito.verify(cartService).deleteFromCart(Mockito.any(Authentication.class), Mockito.anyLong());
    }
}
