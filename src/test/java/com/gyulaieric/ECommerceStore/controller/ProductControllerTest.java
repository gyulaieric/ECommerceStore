package com.gyulaieric.ECommerceStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyulaieric.ECommerceStore.model.Category;
import com.gyulaieric.ECommerceStore.model.Product;
import com.gyulaieric.ECommerceStore.repository.RoleRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import com.gyulaieric.ECommerceStore.service.ProductService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@ExtendWith(MockitoExtension.class)
public class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void getProducts() throws Exception {
        Product product1 = new Product("product1", "testDescription", "/image.png", 9.99, null, false, 40, new Category());
        product1.setId(1L);
        Product product2 = new Product("product2", "testDescription", "/image.png", 9.99, null, false, 40, new Category());
        product2.setId(2L);

        Mockito.when(productService.getProducts()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/api/products/")
                .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(product1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", CoreMatchers.is(product2.getId().intValue())));
    }

    @Test
    public void getProduct() throws Exception {
        Product product = new Product("product1", "testDescription", "/image.png", 9.99, null, false, 40, new Category());
        product.setId(1L);

        Mockito.when(productService.getProduct(product.getId())).thenReturn(product);

        mockMvc.perform(get("/api/products/" + product.getId())
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(product.getId().intValue())));
    }
}
