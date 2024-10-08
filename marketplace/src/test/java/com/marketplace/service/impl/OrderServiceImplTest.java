package com.marketplace.service.impl;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.marketplace.dto.OrderSummaryResponse;
import com.marketplace.enums.OrderStatus;
import com.marketplace.model.*;
import com.marketplace.repository.OrderRepository;
import com.marketplace.repository.ShoppingCartRepository;
import com.marketplace.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ShoppingCartService shoppingCartService;

    @InjectMocks
    private OrderServiceImpl orderService;

    private ShoppingCart mockCart;
    private Order mockOrder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCart = new ShoppingCart();
        mockCart.setId(1L);
        List<CartItem> cartItems = new ArrayList<>();
        CartItem cartItem = new CartItem();
        cartItem.setProduct(new Product());
        cartItem.setQuantity(1);
        cartItem.setPrice(BigDecimal.valueOf(100));
        cartItems.add(cartItem);
        mockCart.setItems(cartItems);
        mockOrder = new Order();
        mockOrder.setId(1L);
        mockOrder.setOrderStatus(OrderStatus.PENDING);
    }

    @Test
    void testCreateOrderFromCart() {
        when(shoppingCartService.getCartById(1L)).thenReturn(mockCart);
        when(orderRepository.save(any(Order.class))).thenReturn(mockOrder);

        Order createdOrder = orderService.createOrderFromCart(1L, 1L);

        assertNotNull(createdOrder);
        assertEquals(OrderStatus.PENDING, createdOrder.getOrderStatus());
        verify(orderRepository).save(any(Order.class));
        verify(shoppingCartService).clearCart(1L);
    }

    @Test
    void testUpdateOrderStatus() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(mockOrder));

        orderService.updateOrderStatus(1L, "SHIPPED");

        assertEquals(OrderStatus.SHIPPED, mockOrder.getOrderStatus());
        verify(orderRepository).save(mockOrder);
    }

    @Test
    void testCalculateOrderTotal() {
        when(shoppingCartService.getCartById(1L)).thenReturn(mockCart);

        BigDecimal total = orderService.calculateOrderTotal(1L);

        assertEquals(BigDecimal.valueOf(100), total);
    }



}