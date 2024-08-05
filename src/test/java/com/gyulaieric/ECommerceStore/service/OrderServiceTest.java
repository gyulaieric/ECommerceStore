package com.gyulaieric.ECommerceStore.service;

import com.gyulaieric.ECommerceStore.model.*;
import com.gyulaieric.ECommerceStore.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private  OrderRepository orderRepository;
    @Mock
    private  OrderedItemRepository orderedItemRepository;
    @Mock
    private  ProductRepository productRepository;
    @Mock
    private  CartRepository cartRepository;
    @Mock
    private  UserRepository userRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Authentication authentication;

    @BeforeEach
    public void setUp() {
        Long userId = 2L;
        user = new User();
        user.setId(userId);
        user.setUsername("testuser");

        authentication = Mockito.mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void testGetOrders() {
        List<Order> orders = Collections.singletonList(new Order());
        Mockito.when(orderRepository.findAll()).thenReturn(orders);

        List<Order> retrievedOrders = orderService.getOrders();

        assertEquals(orders.size(), retrievedOrders.size());
    }

    @Test
    public void getOrderById() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        order.setUserId(user.getId());

        Mockito.when(authentication.getName()).thenReturn("testuser");

        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        Order retrievedOrder = orderService.getOrderById(authentication, orderId);

        assertEquals(orderId, retrievedOrder.getId());
    }

    @Test
    public void getOrderById_NonExistentUser_ShouldThrowIllegalStateException() {
        Mockito.when(authentication.getName()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        Long orderId = 1L;

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.getOrderById(authentication, orderId));

        assertEquals("User " + authentication.getName() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void getOrderById_NonExistentOrder_ShouldThrowIllegalStateException() {
        Mockito.when(authentication.getName()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Long orderId = 1L;
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.getOrderById(authentication, orderId));

        assertEquals("Order with id " + orderId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void getOrderById_UserIsNotOwnerOfOrder_ShouldThrowIllegalStateException() {
        Mockito.when(authentication.getName()).thenReturn("testuser");

        User wrongUser = new User();
        wrongUser.setUsername("wrongUser");
        wrongUser.setId(3L);
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(wrongUser));

        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(user.getId());
        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.getOrderById(authentication, orderId));

        assertEquals("You can only view orders that you have placed", exception.getMessage());
    }

    @Test
    public void getOrdersByUserId() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setUserId(user.getId());

        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(user.getId());

        List<Order> orders = Arrays.asList(order1, order2);

        Mockito.when(authentication.getName()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        Mockito.when(orderRepository.findAllByUserId(user.getId())).thenReturn(orders);

        List<Order> retrievedOrders = orderService.getOrdersByUserId(authentication);

        assertEquals(orders.size(), retrievedOrders.size());
    }

    @Test
    public void getOrdersByUserId_NonExistentUser_ShouldThrowIllegalStateException() {
        Mockito.when(authentication.getName()).thenReturn("testuser");
        Mockito.when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.getOrdersByUserId(authentication));
        assertEquals("User " + authentication.getName() + " doesn't exist", exception.getMessage());
    }

    @Test
    public void addOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);
        order.setUserId(user.getId());

        OrderedItem orderedItem = new OrderedItem();
        order.setOrderedItems(List.of(orderedItem));

        Cart cart = new Cart();
        cart.setQuantity(1);

        Mockito.when(orderRepository.save(Mockito.any())).thenReturn(order);

        Mockito.when(cartRepository.findAllByUserId(order.getUserId())).thenReturn(List.of(cart));

        Long retrievedOrderId = orderService.addOrder(order);
        Mockito.verify(cartRepository).delete(Mockito.any(Cart.class));

        assertEquals(order.getId(), retrievedOrderId);
    }

    @Test
    public void addProductToOrder_ExistentOrderedItem() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        Long productId = 2L;
        Product product = new Product();
        product.setId(productId);

        Long orderedItemId = 3L;
        Integer quantity = 5;
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setId(orderedItemId);
        orderedItem.setProduct(product);
        orderedItem.setQuantity(quantity);

        order.setOrderedItems(List.of(orderedItem));

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.addProductToOrder(orderId, orderedItemId, productId, quantity);

        assertEquals(quantity * 2, orderedItem.getQuantity());
    }

    @Test
    public void addProductToOrder_NonExistentOrderedItem() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        Long productId = 2L;
        Product product = new Product();
        product.setId(productId);

        Long orderedItemId = 3L;
        Integer quantity = 5;
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setId(orderedItemId);
        orderedItem.setProduct(product);
        orderedItem.setQuantity(quantity);

        List<OrderedItem> orderedItems = new ArrayList<>();
        orderedItems.add(orderedItem);

        Long orderedItemNotInOrderId = 4L;

        order.setOrderedItems(orderedItems);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        orderService.addProductToOrder(orderId, orderedItemNotInOrderId, productId, quantity);

        Mockito.verify(orderedItemRepository, Mockito.atLeastOnce()).save(Mockito.any(OrderedItem.class));
        Mockito.verify(orderRepository, Mockito.atLeastOnce()).save(Mockito.any(Order.class));
    }

    @Test
    public void addProductToOrder_NonExistentOrderedItem_NonExistentProduct_ShouldThrowIllegalStateException() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        Long productId = 2L;
        Product product = new Product();
        product.setId(productId);

        Long orderedItemId = 3L;
        Integer quantity = 5;
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setId(orderedItemId);
        orderedItem.setProduct(product);
        orderedItem.setQuantity(quantity);

        List<OrderedItem> orderedItems = new ArrayList<>();
        orderedItems.add(orderedItem);

        Long orderedItemNotInOrderId = 4L;

        order.setOrderedItems(orderedItems);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.addProductToOrder(orderId, orderedItemNotInOrderId, productId, quantity));
        assertEquals("Product with id " + productId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void addProductToOrder_NonexistentOrder_ShouldThrowIllegalStateException() {
        Long orderId = 1L;
        Long productId = 2L;
        Long orderedItemId = 3L;
        Integer quantity = 5;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.addProductToOrder(orderId, orderedItemId, productId, quantity));

        assertEquals("Order with id " + orderId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void deleteProductFromOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        Long orderedItemId = 2L;
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setId(orderedItemId);

        List<OrderedItem> orderedItems = new ArrayList<>();
        orderedItems.add(orderedItem);

        order.setOrderedItems(orderedItems);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteProductFromOrder(orderId, orderedItemId);

        Mockito.verify(orderedItemRepository, Mockito.times(1)).delete(orderedItem);
        Mockito.verify(orderRepository, Mockito.times(1)).save(order);
    }

    @Test
    public void deleteProductFromOrder_NonExistentOrder_ShouldThrowIllegalStateException() {
        Long orderId = 1L;
        Long orderedItemId = 2L;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.deleteProductFromOrder(orderId, orderedItemId));
        assertEquals("Order with id " + orderId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void deleteProductFromOrder_NonExistentOrderedItem_ShouldThrowIllegalStateException() {
        Long orderId = 1L;

        Order order = new Order();
        order.setId(orderId);

        Long orderedItemId = 2L;

        Long orderedItemInOrderId = 3L;
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setId(orderedItemInOrderId);

        List<OrderedItem> orderedItems = new ArrayList<>();
        orderedItems.add(orderedItem);
        order.setOrderedItems(orderedItems);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.deleteProductFromOrder(orderId, orderedItemId));
        assertEquals("Item (" + orderedItemId + ") is not in order", exception.getMessage());
    }

    @Test
    public void updateOrder() {
        Long orderId = 1L;

        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setOrderStatus(OrderStatus.PROCESSING);

        Order updatedOrder = new Order();
        updatedOrder.setFirstName("John");
        updatedOrder.setLastName("Doe");
        updatedOrder.setEmail("john.doe@example.com");
        updatedOrder.setOrderStatus(OrderStatus.SHIPPED);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));

        orderService.updateOrder(orderId, updatedOrder);

        assertEquals(updatedOrder.getFirstName(), existingOrder.getFirstName());
        assertEquals(updatedOrder.getLastName(), existingOrder.getLastName());
        assertEquals(updatedOrder.getEmail(), existingOrder.getEmail());
        assertEquals(updatedOrder.getOrderStatus(), existingOrder.getOrderStatus());
        Mockito.verify(orderRepository, Mockito.times(1)).save(existingOrder);
    }

    @Test
    public void updateOrder_NonExistentOrder_ShouldThrowIllegalStateException() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.updateOrder(orderId, order));
        assertEquals("Order with id " + orderId + " doesn't exist", exception.getMessage());
    }

    @Test
    public void deleteOrder() {
        Long orderId = 1L;
        Order order = new Order();
        order.setId(orderId);

        Long orderedItemId = 2L;
        OrderedItem orderedItem = new OrderedItem();
        orderedItem.setId(orderedItemId);

        List<OrderedItem> orderedItems = new ArrayList<>();
        orderedItems.add(orderedItem);

        order.setOrderedItems(orderedItems);

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        orderService.deleteOrder(orderId);

        Mockito.verify(orderRepository, Mockito.times(1)).findById(orderId);
        Mockito.verify(orderRepository, Mockito.times(1)).deleteById(orderId);
        Mockito.verify(orderedItemRepository, Mockito.times(1)).delete(orderedItem);
    }

    @Test
    public void deleteOrder_NonExistentOrder_ShouldThrowIllegalStateException() {
        Long orderId = 1L;

        Mockito.when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> orderService.deleteOrder(orderId));
        assertEquals("Order with id " + orderId + " doesn't exist", exception.getMessage());
    }
}

