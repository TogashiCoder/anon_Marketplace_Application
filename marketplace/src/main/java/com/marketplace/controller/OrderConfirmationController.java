package com.marketplace.controller;

import com.marketplace.dto.OrderConfirmationDto;
import com.marketplace.service.impl.OrderConfirmationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ordersConfirmation")
@RequiredArgsConstructor
public class OrderConfirmationController {

    private final OrderConfirmationService orderConfirmationService;


    @GetMapping("/{orderId}/confirmation")
    public ResponseEntity<OrderConfirmationDto> getOrderConfirmation(@PathVariable Long orderId) {
        OrderConfirmationDto confirmationDto = orderConfirmationService.getOrderConfirmation(orderId);
        return ResponseEntity.ok(confirmationDto);
    }

}
