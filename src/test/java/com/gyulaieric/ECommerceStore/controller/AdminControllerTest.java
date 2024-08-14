package com.gyulaieric.ECommerceStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyulaieric.ECommerceStore.dto.OrderedItemDTO;
import com.gyulaieric.ECommerceStore.model.*;
import com.gyulaieric.ECommerceStore.repository.OrderRepository;
import com.gyulaieric.ECommerceStore.repository.RoleRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import com.gyulaieric.ECommerceStore.service.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(AdminController.class)
public class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderRepository orderRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private CategoryService categoryService;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private JwtEncoder jwtEncoder;

    @Test
    public void addCategory() throws Exception {
        Category category = new Category();

        mockMvc.perform(post("/admin/api/category/add")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());

        Mockito.verify(categoryService).addCategory(category);
    }

    @Test
    public void updateCategory() throws Exception {
        int categoryId = 1;
        Category category = new Category();

        mockMvc.perform(put("/admin/api/category/update/" + categoryId)
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(category)))
                .andExpect(status().isOk());

        Mockito.verify(categoryService).updateCategory(categoryId, category);
    }

    @Test
    public void deleteCategory() throws Exception {
        int categoryId = 1;

        mockMvc.perform(delete("/admin/api/category/delete/" + categoryId)
                        .with(jwt()))
                .andExpect(status().isOk());

        Mockito.verify(categoryService).deleteCategory(categoryId);
    }

    @Test
    public void addProduct() throws Exception {
        Product product = new Product("testProduct", "testDescription", "testImage.png", 15.99, null, false, 40, null);

        mockMvc.perform(post("/admin/api/product/")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());

        Mockito.verify(productService).addProduct(product);
    }

    @Test
    public void updateProduct() throws Exception {
        Long productId = 1L;
        Product product = new Product("testProduct", "testDescription", "testImage.png", 15.99, null, false, 40, null);

        mockMvc.perform(put("/admin/api/product/" + productId)
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk());

        Mockito.verify(productService).updateProduct(productId, product);
    }

    @Test
    public void deleteProduct() throws Exception {
        Long productId = 1L;

        mockMvc.perform(delete("/admin/api/product/" + productId)
                        .with(jwt()))
                .andExpect(status().isOk());

        Mockito.verify(productService).deleteProduct(productId);
    }

    @Test
    public void testGetOrders() throws Exception {
        Order order1 = new Order(LocalDate.now(), 1L, "Eric", "Gyulai", "gyulaieric@gmail.com", "Faget 82", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.PROCESSING);
        order1.setId(1L);
        Order order2 = new Order(LocalDate.now(), 2L, "Anette", "Batyk", "anette@gmail.com", "Xenopol 31", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.SHIPPED);
        order2.setId(2L);

        Mockito.when(orderService.getOrders()).thenReturn(Arrays.asList(order1, order2));

        mockMvc.perform(get("/admin/api/orders/")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", CoreMatchers.is(1)))
                .andExpect(jsonPath("$.[1].id", CoreMatchers.is(2)));

        Mockito.verify(orderService).getOrders();
    }

    @Test
    public void testUpdateOrder() throws Exception {
        Order order = new Order(LocalDate.now(), 1L, "Eric", "Gyulai", "gyulaieric@gmail.com", "Faget 82", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.PROCESSING);

        mockMvc.perform(put("/admin/api/orders/1")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk());

        Mockito.verify(orderService).updateOrder(1L, order);
    }

    @Test
    public void deleteOrder() throws Exception {
        Long orderId = 1L;

        mockMvc.perform(delete("/admin/api/orders/" + orderId)
                        .with(jwt()))
                .andExpect(status().isOk());

        Mockito.verify(orderService).deleteOrder(orderId);
    }

    @Test
    public void addProductToOrder() throws Exception {
        Long orderId = 1L;
        OrderedItemDTO orderedItemDTO = new OrderedItemDTO(orderId, 1L, 1L, 1);

        mockMvc.perform(post("/admin/api/orders/addProduct")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderedItemDTO)))
                .andExpect(status().isOk());

        Mockito.verify(orderService).addProductToOrder(orderId, orderedItemDTO.getOrderedItemId(), orderedItemDTO.getProductId(), orderedItemDTO.getQuantity());
    }

    @Test
    public void removeProductFromOrder() throws Exception {
        Long orderId = 1L;
        OrderedItemDTO orderedItemDTO = new OrderedItemDTO(orderId, 1L, 1L, 1);

        mockMvc.perform(delete("/admin/api/orders/removeProduct")
                        .with(jwt())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderedItemDTO)))
                .andExpect(status().isOk());

        Mockito.verify(orderService).deleteProductFromOrder(orderId, orderedItemDTO.getOrderedItemId());
    }
}