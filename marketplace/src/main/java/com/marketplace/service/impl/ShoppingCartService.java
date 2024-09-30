package com.marketplace.service.impl;

import com.marketplace.exception.DuplicateProductException;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.model.Buyer;
import com.marketplace.model.CartItem;
import com.marketplace.model.Product;
import com.marketplace.model.ShoppingCart;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.CartItemRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.ShoppingCartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ShoppingCartService {

    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    @Transactional
    public ShoppingCart getOrCreateCart(Long buyerId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "ID", buyerId.toString()));

        return buyer.getShoppingCart() != null ? buyer.getShoppingCart() :
                createNewCart(buyer);
    }

    private ShoppingCart createNewCart(Buyer buyer) {
        ShoppingCart newCart = new ShoppingCart();
        newCart.setBuyer(buyer);
        newCart.setIsActive(true);
        return shoppingCartRepository.save(newCart);
    }

    @Transactional
    public CartItem addItemToCart(Long cartId, Long productId, Integer quantity) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "ID", cartId.toString()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId.toString()));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            throw new DuplicateProductException(productId.toString());
        } else {
            CartItem newItem = new CartItem();
            newItem.setProduct(product);
            newItem.setShoppingCart(cart);
            newItem.setQuantity(quantity);
            newItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
            cart.getItems().add(newItem);
            return cartItemRepository.save(newItem);
        }
    }

    @Transactional
    public void removeItemFromCart(Long cartId, Long itemId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "ID", cartId.toString()));

        cart.getItems().removeIf(item -> item.getId().equals(itemId));
        cartItemRepository.deleteById(itemId);
    }

    @Transactional
    public void updateItemQuantity(Long cartId, Long itemId, Integer newQuantity) {
        CartItem item = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem", "ID", itemId.toString()));

        if (!item.getShoppingCart().getId().equals(cartId)) {
            throw new ResourceNotFoundException("CartItem", "cartId", cartId.toString());
        }

        item.setQuantity(newQuantity);
        item.setPrice(item.getProduct().getPrice().multiply(BigDecimal.valueOf(newQuantity)));
        cartItemRepository.save(item);
    }

    public BigDecimal calculateCartTotal(Long cartId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "ID", cartId.toString()));

        return cart.getItems().stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<CartItem> getCartItems(Long cartId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "ID", cartId.toString()));

        return cart.getItems();
    }

    @Transactional
    public void clearCart(Long cartId) {
        ShoppingCart cart = shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "ID", cartId.toString()));

        cartItemRepository.deleteAll(cart.getItems());
        cart.getItems().clear();
        shoppingCartRepository.save(cart);
    }


    @Transactional
    public ShoppingCart getCartById(Long cartId) {
        return shoppingCartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("ShoppingCart", "ID", cartId.toString()));
    }

}