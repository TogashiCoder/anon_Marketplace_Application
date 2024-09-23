package com.marketplace.repository;

import com.marketplace.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPaypalPaymentId(String paypalPaymentId);
    List<Order> findByBuyerId(Long buyerId);
}
