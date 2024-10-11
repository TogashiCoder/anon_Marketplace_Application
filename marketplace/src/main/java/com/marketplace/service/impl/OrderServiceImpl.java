package com.marketplace.service.impl;

import com.marketplace.enums.OrderStatus;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.model.*;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.repository.ShoppingCartRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.marketplace.dto.OrderDisplayDto;
import com.marketplace.dto.OrderSummaryResponse;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SellerRepository sellerRepository;

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





    //new
    @Override
    public OrderSummaryResponse getOrdersSummary() {
        List<Order> orders = orderRepository.findAll();

        OrderSummaryResponse summary = new OrderSummaryResponse();
        summary.setTotalOrders((long) orders.size());
        summary.setTotalRevenue(calculateTotalRevenue(orders));
        summary.setOrders(orders.stream()
                .map(this::mapToOrderDisplayDto)
                .collect(Collectors.toList()));

        return summary;
    }

    @Override
    public List<OrderDisplayDto> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::mapToOrderDisplayDto)
                .collect(Collectors.toList());
    }



    private BigDecimal calculateTotalRevenue(List<Order> orders) {
        return orders.stream()
                .map(Order::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    @Override
    public OrderSummaryResponse getSellerOrdersSummary(Long sellerId) {
        List<Order> sellerOrders = getOrdersForSeller(sellerId);

        OrderSummaryResponse summary = new OrderSummaryResponse();
        summary.setTotalOrders((long) sellerOrders.size());
        summary.setTotalRevenue(calculateSellerTotalRevenue(sellerOrders));
        summary.setOrders(sellerOrders.stream()
                .map(this::mapToOrderDisplayDto)
                .collect(Collectors.toList()));

        return summary;
    }

    @Override
    public List<OrderDisplayDto> getSellerOrders(Long sellerId) {
        return getOrdersForSeller(sellerId).stream()
                .map(this::mapToOrderDisplayDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateSellerOrderStatus(Long sellerId, Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        // Verify the order belongs to the seller
        boolean isSellerOrder = order.getOrderItems().stream()
                .anyMatch(item -> item.getSeller().getId().equals(sellerId));

        if (!isSellerOrder) {
            throw new IllegalArgumentException("Order does not belong to seller");
        }

        try {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid order status: " + status);
        }
    }

    private BigDecimal calculateSellerTotalRevenue(List<Order> orders) {
        return orders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private OrderDisplayDto mapToOrderDisplayDto(Order order) {
        OrderDisplayDto dto = new OrderDisplayDto();
        dto.setId(order.getId().toString());
        dto.setCustomer(order.getBuyer().getFirstname() + " " + order.getBuyer().getLastname());
        dto.setDate(order.getOrderDate().format(DateTimeFormatter.ISO_LOCAL_DATE));
        dto.setTotal(order.getTotalPrice());
        dto.setStatus(formatOrderStatus(order.getOrderStatus())); // Use helper method to format status
        return dto;
    }
    private String formatOrderStatus(OrderStatus status) {
        // Convert enum to properly capitalized string to match frontend
        return status.toString().charAt(0) +
                status.toString().substring(1).toLowerCase();
    }


    private List<Order> getOrdersForSeller(Long sellerId) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(item -> item.getSeller().getId().equals(sellerId)))
                .collect(Collectors.toList());
    }



    @Override
    public boolean hasBuyerPurchasedProduct(Long buyerId, Long productId) {
        List<Order> buyerOrders = orderRepository.findByBuyerId(buyerId);

        return buyerOrders.stream()
                .flatMap(order -> order.getOrderItems().stream())
                .anyMatch(orderItem -> orderItem.getProduct().getId().equals(productId));
    }

    @Override
    public Integer getTotalOrderNumberForSeller(Long sellerId) {
        // Check if the seller exists
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller","id",sellerId.toString()));

        List<Order> allOrders = orderRepository.findAll();

        int totalOrders = (int) allOrders.stream()
                .filter(order -> order.getOrderItems().stream()
                        .anyMatch(item -> item.getSeller().getId().equals(sellerId)))
                .count();

        return totalOrders;
    }


}