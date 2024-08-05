package com.gyulaieric.ECommerceStore.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderedItemDTO {
    @NotNull(message = "Order Id should not be null")
    private Long orderId;
    //@NotNull(message = "Ordered item Id should not be null")
    private Long orderedItemId;
    @NotNull(message = "Product Id should not be null")
    private Long productId;
    @NotNull(message = "Quantity should not be null")
    @Min(1)
    private int quantity;

    public OrderedItemDTO() { super(); }

    public OrderedItemDTO(Long orderId, Long orderedItemId, Long productId, int quantity) {
        super();
        this.orderId = orderId;
        this.orderedItemId = orderedItemId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderedItemId() {
        return orderedItemId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
