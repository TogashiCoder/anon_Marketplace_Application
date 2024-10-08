package com.marketplace.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.service.impl.ShoppingCartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ShoppingCartServiceTest {


    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @InjectMocks
    private ShoppingCartService shoppingCartService;

    private ShoppingCart mockCart;
    private CartItem mockCartItem;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        mockCart = new ShoppingCart();
        mockCart.setId(1L);

        mockCartItem = new CartItem();
        mockCartItem.setId(1L);
        mockCartItem.setPrice(BigDecimal.valueOf(100));
        mockCartItem.setQuantity(1);

        mockCart.setItems(List.of(mockCartItem));
    }

    @Test
    void testGetOrCreateCart_CreatesNewCart() {
        Buyer buyer = new Buyer();
        buyer.setId(1L);
        when(buyerRepository.findById(1L)).thenReturn(Optional.of(buyer));
        when(shoppingCartRepository.save(any(ShoppingCart.class))).thenReturn(mockCart);

        ShoppingCart cart = shoppingCartService.getOrCreateCart(1L);

        assertNotNull(cart);
        assertEquals(1L, cart.getId());
    }



    @Test
    void testCalculateCartTotal() {
        when(shoppingCartRepository.findById(1L)).thenReturn(Optional.of(mockCart));

        BigDecimal total = shoppingCartService.calculateCartTotal(1L);

        assertEquals(BigDecimal.valueOf(100), total);
    }





}