package com.marketplace.controller;

import com.marketplace.dto.OrderDisplayDto;
import com.marketplace.dto.OrderSummaryResponse;
import com.marketplace.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    // Get summary of all orders
    @GetMapping("/summary")
    public ResponseEntity<OrderSummaryResponse> getOrdersSummary() {
        OrderSummaryResponse summary = orderService.getOrdersSummary();
        return ResponseEntity.ok(summary);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<List<OrderDisplayDto>> getAllOrders() {
        List<OrderDisplayDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Get summary of seller orders by sellerId
    @GetMapping("/seller/{sellerId}/summary")
    public ResponseEntity<OrderSummaryResponse> getSellerOrdersSummary(@PathVariable Long sellerId) {
        OrderSummaryResponse summary = orderService.getSellerOrdersSummary(sellerId);
        return ResponseEntity.ok(summary);
    }

    // Get all orders for a specific seller
    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<OrderDisplayDto>> getSellerOrders(@PathVariable Long sellerId) {
        List<OrderDisplayDto> sellerOrders = orderService.getSellerOrders(sellerId);
        return ResponseEntity.ok(sellerOrders);
    }

    // Update the status of a seller's order
    @PutMapping("/seller/{sellerId}/order/{orderId}/status/{status}")
    public ResponseEntity<Void> updateSellerOrderStatus(
            @PathVariable Long sellerId,
            @PathVariable Long orderId,
            @PathVariable String status) {
        orderService.updateSellerOrderStatus(sellerId, orderId, status);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/buyer/{buyerId}/product/{productId}/purchased")
    public ResponseEntity<Boolean> hasBuyerPurchasedProduct(
            @PathVariable Long buyerId,
            @PathVariable Long productId) {
        boolean hasPurchased = orderService.hasBuyerPurchasedProduct(buyerId, productId);
        return ResponseEntity.ok(hasPurchased);
    }


    @GetMapping("/count/orderNumber/by/seller/{sellerId}")
    public ResponseEntity<Integer> getOrderNumberBySellerId(@PathVariable Long sellerId) {
        Integer count = orderService.getTotalOrderNumberForSeller(sellerId);
        return ResponseEntity.ok(count);
    }




}
