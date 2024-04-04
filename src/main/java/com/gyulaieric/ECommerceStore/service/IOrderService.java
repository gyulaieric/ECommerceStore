package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.Order;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface IOrderService {
    List<Order> getOrders();
    Order getOrderById(Long id);
    List<Order> getOrdersByUserId(Authentication authentication);
    Long addOrder(Order order);
    void addProductToOrder(Long id, Long orderedItemId, Long productID, int quantity);
    void deleteProductFromOrder(Long id, Long orderedItemId);
    void updateOrder(Long id, Order order);
    void deleteOrder(Long id);
}
