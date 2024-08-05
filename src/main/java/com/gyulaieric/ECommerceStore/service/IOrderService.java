package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Order;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    List<Order> getOrders();
    Order getOrderById(Authentication authentication, Long id);
    List<Order> getOrdersByUserId(Authentication authentication);
    Long addOrder(Order order);
    void addProductToOrder(Long id, Long orderedItemId, Long productID, Integer quantity);
    void deleteProductFromOrder(Long id, Long orderedItemId);
    void updateOrder(Long id, Order order);
    void deleteOrder(Long id);
}
