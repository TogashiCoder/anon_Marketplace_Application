package com.marketplace.service.impl;

import com.marketplace.dto.OrderConfirmationDto;
import com.marketplace.dto.OrderItemDto;
import com.marketplace.model.Order;
import com.marketplace.model.OrderItem;
import com.marketplace.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderConfirmationService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderConfirmationService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public OrderConfirmationDto getOrderConfirmation(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        return mapOrderToConfirmationDto(order);
    }

    private OrderConfirmationDto mapOrderToConfirmationDto(Order order) {
        OrderConfirmationDto dto = new OrderConfirmationDto();
        dto.setId(order.getId());
        dto.setOrderNumber(order.getId().toString()); // Assuming order number is the same as ID
        dto.setOrderDate(order.getOrderDate());
        dto.setOrderStatus(order.getOrderStatus());
        dto.setTotalAmount(order.getTotalPrice());
        dto.setEstimatedDeliveryDate(order.getShipmentDetails().getEstimatedDeliveryDate());
        dto.setOrderItems(mapOrderItems(order.getOrderItems()));
        dto.setBuyerName(order.getBuyer().getFirstname() + " " + order.getBuyer().getLastname());
        dto.setCarrierName(order.getShipmentDetails().getCarrierName());
        dto.setTrackingNumber(order.getShipmentDetails().getTrackingNumber());
        dto.setPaymentType(order.getPaymentMethod().getPaymentType());
        dto.setPaymentDetails(order.getPaymentMethod().getDetails());
        dto.setPaypalPaymentId(order.getPaypalPaymentId());

        // Set shipping details
        dto.setShippingStreet(order.getBuyer().getStreet());
        dto.setShippingCity(order.getBuyer().getCity());
        dto.setShippingState(order.getBuyer().getState());
        dto.setShippingZipCode(order.getBuyer().getZipcode());
        dto.setShippingCountry(order.getBuyer().getCountry());

        return dto;
    }

    private List<OrderItemDto> mapOrderItems(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(this::mapOrderItemToDto)
                .collect(Collectors.toList());
    }

    private OrderItemDto mapOrderItemToDto(OrderItem orderItem) {
        OrderItemDto dto = new OrderItemDto();
        dto.setProductName(orderItem.getProduct().getName());
        dto.setQuantity(orderItem.getQuantity());
        dto.setPrice(orderItem.getPrice());

        BigDecimal itemTotalPrice = orderItem.getPrice().multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        dto.setTotalPrice(itemTotalPrice);
        return dto;
    }

}