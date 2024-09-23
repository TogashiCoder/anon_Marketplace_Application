package com.marketplace.service.impl;

import com.marketplace.enums.OrderStatus;
import com.marketplace.model.*;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ShoppingCartRepository;
import com.marketplace.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ShoppingCartService shoppingCartService;

    // Create an order based on the shopping cart
    @Override
    public Order createOrderFromCart(Long cartId, Long buyerId) {
        ShoppingCart cart = shoppingCartService.getCartById(cartId);

        if (cart == null || cart.getItems().isEmpty()) {
            throw new IllegalArgumentException("Cart is empty or not found.");
        }

        // Convert CartItems to OrderItems
        List<OrderItem> orderItems = cart.getItems().stream().map(cartItem -> {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setSeller(cartItem.getProduct().getSeller()); // Assuming each product has a seller
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getPrice()); // Assuming CartItem has price, otherwise calculate it
            return orderItem;
        }).collect(Collectors.toList());

        Order order = new Order();
        order.setBuyer(cart.getBuyer()); // Setting the buyer
        order.setOrderItems(orderItems); // Transferring cart items to order items
        order.setOrderStatus(OrderStatus.PENDING); // Initial status
        order.setTotalPrice(calculateOrderTotal(cartId)); // Calculate total price
        order.setOrderDate(LocalDateTime.now()); // Set order date

        // Save the order to the database
        Order savedOrder = orderRepository.save(order);

        // Clear the shopping cart after checkout
        shoppingCartService.clearCart(cartId);

        return savedOrder;
    }

    // Update the order status
    @Override
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setOrderStatus(OrderStatus.valueOf(status));
        orderRepository.save(order);
    }

    // Save order to database
    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    // Calculate total order price based on cart items
    @Override
    public BigDecimal calculateOrderTotal(Long cartId) {
        ShoppingCart cart = shoppingCartService.getCartById(cartId);
        return cart.getItems()
                .stream()
                .map(cartItem -> cartItem.getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found with ID: " + orderId));
    }


}
