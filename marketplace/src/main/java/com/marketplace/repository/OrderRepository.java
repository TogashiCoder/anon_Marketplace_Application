package com.marketplace.repository;

import com.marketplace.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.marketplace.enums.OrderStatus;
import org.springframework.data.domain.Pageable;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByPaypalPaymentId(String paypalPaymentId);
    List<Order> findByBuyerId(Long buyerId);


    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.seller.id = :sellerId")
    List<Order> findBySellerId(@Param("sellerId") Long sellerId, Pageable pageable);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.seller.id = :sellerId AND o.orderStatus = :status")
    List<Order> findBySellerIdAndOrderStatus(@Param("sellerId") Long sellerId, @Param("status") OrderStatus status, Pageable pageable);

    @Query("SELECT COUNT(DISTINCT o) FROM Order o JOIN o.orderItems oi WHERE oi.seller.id = :sellerId AND o.orderDate BETWEEN :startDate AND :endDate")
    int countBySellerIdAndOrderDateBetween(@Param("sellerId") Long sellerId, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems oi WHERE oi.seller.id = :sellerId")
    List<Order> findBySellerId(@Param("sellerId") Long sellerId);
}
