package com.gyulaieric.ECommerceStore.repository;

import com.gyulaieric.ECommerceStore.model.Cart;
import com.gyulaieric.ECommerceStore.model.OrderedItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderedItemRepository extends JpaRepository<OrderedItem, Long> {
}
