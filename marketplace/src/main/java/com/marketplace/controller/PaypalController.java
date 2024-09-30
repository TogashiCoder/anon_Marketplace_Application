package com.marketplace.controller;


import com.marketplace.service.paymentService.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
@RequestMapping("/api/paypal")
public class PaypalController {

    private final PaypalService paypalService;

    @PostMapping("/create")
    public ResponseEntity<?> createPayment(@RequestBody Map<String, String> payload) {
        try {
            String cancelUrl = "http://localhost:4200/payment/cancel";
            String successUrl = "http://localhost:4200/payment/success";
            Payment payment = paypalService.createPayment(
                    new BigDecimal(payload.get("amount")),
                    payload.get("currency"),
                    payload.get("method"),
                    "sale",
                    payload.get("description"),
                    cancelUrl,
                    successUrl
            );

            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("approvalUrl", links.getHref());
                    return ResponseEntity.ok(response);
                }
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "No approval URL found"));
    }

    @PostMapping("/execute")
    public ResponseEntity<?> executePayment(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId
    ) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                // Here you would typically update your order status and save the PayPal payment ID
                // orderService.updateOrderStatus(orderId, OrderStatus.PAID);
                // orderService.savePayPalPaymentId(orderId, paymentId);
                return ResponseEntity.ok(Map.of("message", "Payment approved"));
            }
        } catch (PayPalRESTException e) {
            log.error("Error occurred:: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "An error occurred"));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Payment not approved"));
    }
}