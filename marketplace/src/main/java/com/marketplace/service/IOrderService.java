package com.marketplace.service;

import com.marketplace.dto.OrderDisplayDto;
import com.marketplace.dto.OrderSummaryResponse;
import com.marketplace.model.Order;

import java.math.BigDecimal;
import java.util.List;

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


    OrderSummaryResponse getOrdersSummary();
    List<OrderDisplayDto> getAllOrders();
    OrderSummaryResponse getSellerOrdersSummary(Long sellerId);
    List<OrderDisplayDto> getSellerOrders(Long sellerId);
    void updateSellerOrderStatus(Long sellerId, Long orderId, String status);



    boolean hasBuyerPurchasedProduct(Long buyerId, Long productId);



    Integer getTotalOrderNumberForSeller(Long sellerId);


}