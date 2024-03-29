package com.gyulaieric.ECommerceStore.model;

public class OrderedItemDTO {
    private Long orderId;
    private Long orderedItemId;
    private Long productId;
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
