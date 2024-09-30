package com.marketplace.controller;

import com.marketplace.enums.OrderStatus;
import com.marketplace.model.CartItem;
import com.marketplace.model.Order;
import com.marketplace.model.ShoppingCart;
import com.marketplace.service.IOrderService;
import com.marketplace.service.impl.ShoppingCartService;
import com.marketplace.service.paymentService.PaypalService;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ShoppingCartController {


    private final ShoppingCartService shoppingCartService;


    private final PaypalService paypalService;


    private final IOrderService orderService;




    // Get or create a shopping cart for a buyer
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ShoppingCart> getOrCreateCart(@PathVariable Long buyerId) {
        ShoppingCart cart = shoppingCartService.getOrCreateCart(buyerId);
        return ResponseEntity.ok(cart);
    }

    // Add an item to the cart
    @PostMapping("/{cartId}/add-item")
    public ResponseEntity<CartItem> addItemToCart(
            @PathVariable Long cartId,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {
        CartItem cartItem = shoppingCartService.addItemToCart(cartId, productId, quantity);
        return ResponseEntity.ok(cartItem);
    }

    // Remove an item from the cart
    @DeleteMapping("/{cartId}/remove-item/{itemId}")
    public ResponseEntity<Void> removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        shoppingCartService.removeItemFromCart(cartId, itemId);
        return ResponseEntity.ok().build();
    }

    // Update the quantity of an item
    @PutMapping("/{cartId}/update-item/{itemId}")
    public ResponseEntity<CartItem> updateItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @RequestParam Integer quantity) {
        shoppingCartService.updateItemQuantity(cartId, itemId, quantity);
        return ResponseEntity.ok().build();
    }

    // Calculate the total of the cart
    @GetMapping("/{cartId}/total")
    public ResponseEntity<BigDecimal> calculateCartTotal(@PathVariable Long cartId) {
        BigDecimal total = shoppingCartService.calculateCartTotal(cartId);
        return ResponseEntity.ok(total);
    }

    // Get all items in a cart
    @GetMapping("/{cartId}/items")
    public ResponseEntity<List<CartItem>> getCartItems(@PathVariable Long cartId) {
        List<CartItem> cartItems = shoppingCartService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    // Clear all items from the cart
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        shoppingCartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }


    // Create an order from the cart and initiate payment
    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<Map<String, String>> checkoutCart(
            @PathVariable Long cartId,
            @RequestParam Long buyerId
    ) {
        // Create the order from the shopping cart
        Order order = orderService.createOrderFromCart(cartId, buyerId);
        BigDecimal totalAmount = order.getTotalPrice();

        // Prepare PayPal payment
        String cancelUrl = "http://localhost:8080/api/cart/payment/cancel";
        String successUrl = "http://localhost:8080/api/cart/payment/success?orderId=" + order.getId();
        try {
            Payment payment = paypalService.createPayment(
                    totalAmount,
                    "USD",
                    "paypal",
                    "sale",
                    "Payment for order " + order.getId(),
                    cancelUrl,
                    successUrl
            );

            // Return PayPal approval URL
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("approvalUrl", links.getHref());
                    return ResponseEntity.ok(response);
                }
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Payment creation failed"));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "No approval URL found"));
    }

    // PayPal payment success handler
    @GetMapping("/payment/success")
    public ResponseEntity<String> paymentSuccess(@RequestParam("orderId") Long orderId, @RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId) {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if ("approved".equals(payment.getState())) {
                // Payment is successful, handle order completion here
                Order order = orderService.getOrderById(orderId);
                order.setOrderStatus(OrderStatus.COMPLETED);
                orderService.saveOrder(order);
                return ResponseEntity.ok("Payment successful and order completed");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment failed");
            }
        } catch (PayPalRESTException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Payment execution failed");
        }
    }

    // PayPal payment cancel handler
    @GetMapping("/payment/cancel")
    public ResponseEntity<String> paymentCancel() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Payment was canceled");
    }
}
