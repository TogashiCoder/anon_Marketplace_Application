package com.marketplace.service.impl;

import com.marketplace.dto.CouponDto;
import com.marketplace.exception.*;
import com.marketplace.mapper.CouponMapper;
import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.service.impl.ICouponServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ICouponServiceImplTest {

    @InjectMocks
    private ICouponServiceImpl couponService;

    @Mock
    private CouponRepository couponRepository;

    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private BuyerRepository buyerRepository;

    @Mock
    private CouponMapper couponMapper;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CouponUsageRepository couponUsageRepository;

    @Mock
    private ShoppingCartRepository shoppingCartRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCoupon_WhenCouponCodeAlreadyExists_ShouldThrowException() {
        // Arrange
        CouponDto couponDto = new CouponDto();
        couponDto.setCode("EXISTINGCODE");
        when(couponRepository.existsByCode("EXISTINGCODE")).thenReturn(true);

        // Act & Assert
        assertThrows(CouponAlreadyUsedException.class, () -> couponService.createCoupon(couponDto, 1L));
        verify(couponRepository, never()).save(any());
    }

//    @Test
//    void createCoupon_WhenValid_ShouldSaveCoupon() {
//        // Arrange
//        CouponDto couponDto = new CouponDto();
//        couponDto.setCode("NEWCODE");
//
//        Seller seller = new Seller();
//        Coupon coupon = new Coupon();
//        Coupon savedCoupon = new Coupon();
//
//        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
//        when(couponMapper.toEntity(couponDto)).thenReturn(coupon);
//        when(couponRepository.save(coupon)).thenReturn(savedCoupon);
//        when(couponMapper.toDto(savedCoupon)).thenReturn(couponDto);
//
//        // Act
//        CouponDto result = couponService.createCoupon(couponDto, 1L);
//
//        // Assert
//        assertEquals(couponDto, result);
//        verify(couponRepository).save(coupon);
//    }

    @Test
    void updateCoupon_WhenCouponNotFound_ShouldThrowException() {
        // Arrange
        when(couponRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CouponNotFoundException.class, () -> couponService.updateCoupon(1L, new CouponDto()));
    }



    @Test
    void deleteCouponByCode_WhenCouponNotFound_ShouldThrowException() {
        // Arrange
        when(couponRepository.findByCode("CODE123")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CouponNotFoundException.class, () -> couponService.deleteCouponByCode("CODE123"));
    }

    @Test
    void deleteCouponByCode_WhenValid_ShouldDeleteCoupon() {
        // Arrange
        Coupon coupon = new Coupon();
        when(couponRepository.findByCode("CODE123")).thenReturn(Optional.of(coupon));

        // Act
        couponService.deleteCouponByCode("CODE123");

        // Assert
        verify(couponRepository).deleteById(coupon.getId());
    }

    @Test
    void applyCouponToProduct_WhenCouponNotValid_ShouldThrowException() {
        // Arrange
        Coupon coupon = new Coupon();
        coupon.setStartDate(LocalDate.now().minusDays(2));
        coupon.setEndDate(LocalDate.now().minusDays(1));

        when(couponRepository.findById(1L)).thenReturn(Optional.of(coupon));
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        when(buyerRepository.findById(1L)).thenReturn(Optional.of(new Buyer()));

        // Act & Assert
        assertThrows(CouponExpiredException.class, () -> couponService.applyCouponToProduct(1L, 1L, 1L));
    }

}