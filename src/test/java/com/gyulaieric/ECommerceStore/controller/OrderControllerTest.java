package com.gyulaieric.ECommerceStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gyulaieric.ECommerceStore.model.Order;
import com.gyulaieric.ECommerceStore.model.OrderStatus;
import com.gyulaieric.ECommerceStore.repository.RoleRepository;
import com.gyulaieric.ECommerceStore.repository.UserRepository;
import com.gyulaieric.ECommerceStore.service.OrderService;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void addOrder() throws Exception {
        Order order = new Order(LocalDate.now(), 1L, "Eric", "Gyulai", "gyulaieric@gmail.com", "Faget 82", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.PROCESSING);

        mockMvc.perform(post("/api/orders/")
                .with(jwt())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", CoreMatchers.is(0)));
    }

    @Test
    public void getOrder() throws Exception {
        Order order = new Order(LocalDate.now(), 1L, "Eric", "Gyulai", "gyulaieric@gmail.com", "Faget 82", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.PROCESSING);
        order.setId(1L);

        Mockito.when(orderService.getOrderById(Mockito.any(Authentication.class), Mockito.anyLong())).thenReturn(order);

        mockMvc.perform(get("/api/orders/" + order.getId())
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", CoreMatchers.is(order.getId().intValue())));
    }

    @Test
    public void getOrdersByUserId() throws Exception {
        Order order1 = new Order(LocalDate.now(), 1L, "Eric", "Gyulai", "gyulaieric@gmail.com", "Faget 82", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.PROCESSING);
        order1.setId(1L);
        Order order2 = new Order(LocalDate.now(), 1L, "Eric", "Gyulai", "gyulaieric@gmail.com", "Faget 82", "Romania", "Maramures", "Sighetu Marmatiei", "435500", null, OrderStatus.PROCESSING);
        order2.setId(2L);

        Mockito.when(orderService.getOrdersByUserId(Mockito.any(Authentication.class))).thenReturn(List.of(order1, order2));

        mockMvc.perform(get("/api/orders/")
                        .with(jwt()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", CoreMatchers.is(order1.getId().intValue())))
                .andExpect(jsonPath("$[1].id", CoreMatchers.is(order2.getId().intValue())));
    }
}
