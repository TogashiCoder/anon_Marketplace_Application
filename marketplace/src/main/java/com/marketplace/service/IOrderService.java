package com.marketplace.service;

import com.marketplace.model.Order;

import java.math.BigDecimal;

public interface IOrderService {
    // Method to create an order from a cart
    Order createOrderFromCart(Long cartId, Long buyerId);

    // Method to update order status
    void updateOrderStatus(Long orderId, String status);

    // Method to save the total price of the order
    Order saveOrder(Order order);

    // Method to calculate the total price of the order
    BigDecimal calculateOrderTotal(Long cartId);

    Order getOrderById(Long orderId);

}
