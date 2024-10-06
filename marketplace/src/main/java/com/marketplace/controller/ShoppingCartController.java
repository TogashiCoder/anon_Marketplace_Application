package com.marketplace.controller;

import com.marketplace.dto.CartItemDto;
import com.marketplace.enums.OrderStatus;
import com.marketplace.enums.PaymentType;
import com.marketplace.exception.CartOwnershipException;
import com.marketplace.exception.EmptyCartException;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.model.*;
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
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @PostMapping("/{cartId}/add-item/{productId}/{quantity}")
    public ResponseEntity<CartItem> addItemToCart(
            @PathVariable Long cartId,
            @PathVariable Long productId,
            @PathVariable Integer quantity) {
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
    @PutMapping("/{cartId}/update-item/{itemId}/{quantity}")
    public ResponseEntity<CartItem> updateItemQuantity(
            @PathVariable Long cartId,
            @PathVariable Long itemId,
            @PathVariable Integer quantity) {
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
    public ResponseEntity<List<CartItemDto>> getCartItems(@PathVariable Long cartId) {
        List<CartItemDto> cartItems = shoppingCartService.getCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    // Clear all items from the cart
    @DeleteMapping("/{cartId}/clear")
    public ResponseEntity<Void> clearCart(@PathVariable Long cartId) {
        shoppingCartService.clearCart(cartId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/{cartId}/checkout/{buyerId}")
    public ResponseEntity<Map<String, String>> checkoutCart(
            @PathVariable Long cartId,
            @PathVariable Long buyerId
    ) {
        try {
            // Get the cart
            ShoppingCart cart = shoppingCartService.getCartById(cartId);

            if (cart.getItems().isEmpty()) {
                throw new EmptyCartException("The shopping cart is empty");
            }

            // Create the order from the shopping cart
            Order order = new Order();
            order.setBuyer(cart.getBuyer());
            order.setOrderStatus(OrderStatus.PENDING);
            order.setOrderDate(LocalDateTime.now());

            // Convert CartItems to OrderItems
            List<OrderItem> orderItems = cart.getItems().stream()
                    .map(this::convertCartItemToOrderItem)
                    .collect(Collectors.toList());

            // Set the order reference for each OrderItem
            Order finalOrder = order;
            orderItems.forEach(orderItem -> orderItem.setOrder(finalOrder));

            order.setOrderItems(orderItems);
            order.setTotalPrice(calculateOrderTotal(cart));

            // Create and set ShipmentDetails
            ShipmentDetails shipmentDetails = new ShipmentDetails();
            shipmentDetails.setCarrierName("Default Carrier");
            shipmentDetails.setTrackingNumber("TBD");
            shipmentDetails.setShipmentDate(LocalDateTime.now());
            shipmentDetails.setEstimatedDeliveryDate(LocalDateTime.now().plusDays(7));
            order.setShipmentDetails(shipmentDetails);

            // Create and set PaymentMethod (PayPal for now)
            PaymentMethod paymentMethod = new PaymentMethod();
            paymentMethod.setPaymentType(PaymentType.PAYPAL);
            paymentMethod.setDetails("PayPal Payment");
            order.setPaymentMethod(paymentMethod);

            // Save the order
            order = orderService.saveOrder(order);

            BigDecimal totalAmount = order.getTotalPrice();

            // Prepare PayPal payment
            String cancelUrl = "http://localhost:8080/api/cart/payment/cancel";
            String successUrl = "http://localhost:4200/buyer/order-confirmation/"+order.getId();


            Payment payment = paypalService.createPayment(
                    totalAmount,
                    "USD",
                    "paypal",
                    "sale",
                    "Payment for order " + order.getId(),
                    cancelUrl,
                    successUrl
            );

            // Clear the shopping cart after successful payment creation
            shoppingCartService.clearCart(cartId);

            // Return PayPal approval URL
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    Map<String, String> response = new HashMap<>();
                    response.put("approvalUrl", links.getHref());
                    return ResponseEntity.ok(response);
                }
            }
            // If no approval URL is found
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "No approval URL found"));

        } catch (EmptyCartException e) {
            // Handle case where the cart is empty
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (CartOwnershipException e) {
            // Handle case where the cart does not belong to the buyer
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", e.getMessage()));
        } catch (ResourceNotFoundException e) {
            // Handle case where a resource (order, cart, etc.) was not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        } catch (PayPalRESTException e) {
            // Handle PayPal payment creation failure
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Payment creation failed"));
        } catch (Exception e) {
            // Handle unexpected errors
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "An unexpected error occurred"));
        }
    }

    private OrderItem convertCartItemToOrderItem(CartItem cartItem) {
        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(cartItem.getProduct());
        orderItem.setSeller(cartItem.getProduct().getSeller());
        orderItem.setQuantity(cartItem.getQuantity());
        orderItem.setPrice(cartItem.getProduct().getPrice());
        return orderItem;
    }

    private BigDecimal calculateOrderTotal(ShoppingCart cart) {
        return cart.getItems().stream()
                .map(cartItem -> cartItem.getProduct().getPrice().multiply(new BigDecimal(cartItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    // Retrieve cart ID by buyer ID
    @GetMapping("/buyer/{buyerId}/cart-id")
    public ResponseEntity<Long> getCartIdByBuyerId(@PathVariable Long buyerId) {
        try {
            Long cartId = shoppingCartService.getCartIdByBuyerId(buyerId);
            return ResponseEntity.ok(cartId);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    // Endpoint to check if a product exists in the cart
    @GetMapping("/{cartId}/contains-product/{productId}")
    public ResponseEntity<Boolean> hasProductInCart(@PathVariable Long cartId, @PathVariable Long productId) {
        boolean hasProduct = shoppingCartService.hasProductInCart(cartId, productId);
        return ResponseEntity.ok(hasProduct);
    }

}
