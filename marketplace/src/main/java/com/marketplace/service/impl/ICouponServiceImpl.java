package com.marketplace.service.impl;

import com.marketplace.dto.CouponDto;
import com.marketplace.exception.*;
import com.marketplace.mapper.CouponMapper;
import com.marketplace.model.Coupon;
import com.marketplace.model.Product;
import com.marketplace.model.Seller;
import com.marketplace.repository.CouponRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICouponServiceImpl implements ICouponService {

    private static final Logger logger = LoggerFactory.getLogger(ICouponServiceImpl.class);

    private final CouponRepository couponRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final CouponMapper couponMapper;

    @Override
    public CouponDto createCoupon(CouponDto couponDto) {
        if (isCouponCodeAlreadyUsed(couponDto.getCode())) {
            logger.error("Coupon creation failed: Coupon code {} is already used.", couponDto.getCode());
            throw new CouponAlreadyUsedException("Coupon code already in use");
        }

        Coupon coupon = couponMapper.toEntity(couponDto);
        coupon.setRedeemCount(0); // Initialize redeem count
        try {
            Coupon savedCoupon = couponRepository.save(coupon);
            logger.info("Coupon created successfully: {}", savedCoupon.getCode());
            return couponMapper.toDto(savedCoupon);
        } catch (Exception e) {
            logger.error("Error occurred while creating coupon: ", e);
            throw new CouponCreationException("Failed to create coupon");
        }
    }

    @Override
    public CouponDto updateCoupon(Long id, CouponDto couponDto) {
        Coupon existingCoupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));

        couponMapper.updateEntityFromDto(couponDto, existingCoupon);

        try {
            Coupon updatedCoupon = couponRepository.save(existingCoupon);
            logger.info("Coupon updated successfully: {}", updatedCoupon.getCode());
            return couponMapper.toDto(updatedCoupon);
        } catch (Exception e) {
            logger.error("Error occurred while updating coupon: ", e);
            throw new CouponCreationException("Failed to update coupon");
        }
    }

    @Override
    public void deleteCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with code: " + code));

        dissociateCouponFromProducts(coupon);

        try {
            couponRepository.deleteById(coupon.getId());
            logger.info("Coupon deleted successfully: Code {}", code);
        } catch (Exception e) {
            logger.error("Error occurred while deleting coupon: ", e);
            throw new CouponCreationException("Failed to delete coupon");
        }
    }

    @Override
    public void deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));

        dissociateCouponFromProducts(coupon);

        try {
            couponRepository.deleteById(id);
            logger.info("Coupon deleted successfully: ID {}", id);
        } catch (Exception e) {
            logger.error("Error occurred while deleting coupon: ", e);
            throw new CouponCreationException("Failed to delete coupon");
        }
    }

    private void dissociateCouponFromProducts(Coupon coupon) {
        List<Product> associatedProducts = productRepository.findByCoupon(coupon);
        for (Product product : associatedProducts) {
            product.setCoupon(null);
            productRepository.save(product);
        }
    }

    @Override
    public CouponDto getCouponById(Long id) {
        Coupon coupon = couponRepository.findById(id)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + id));

        logger.info("Coupon retrieved successfully: ID {}", id);
        return couponMapper.toDto(coupon);
    }

    @Override
    public List<CouponDto> getAllCouponsForASeller(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller not found with id: ", "SellerId", sellerId.toString()));

        return seller.getProducts().stream()
                .map(Product::getCoupon)
                .filter(coupon -> coupon != null)
                .distinct()
                .map(couponMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void removeCouponFromProduct(Long couponId, Long productId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + couponId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: ", "ProductId", productId.toString()));

        if (product.getCoupon() != null && product.getCoupon().equals(coupon)) {
            product.setCoupon(null);
            productRepository.save(product);
            logger.info("Coupon {} removed from product {}", couponId, productId);
        } else {
            logger.warn("Coupon {} is not applied to product {}", couponId, productId);
            throw new CouponNotAppliedException("Coupon is not applied to this product");
        }
    }

    @Override
    public CouponDto getCouponByCode(String code) {
        Coupon coupon = couponRepository.findByCode(code)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with code: " + code));

        logger.info("Coupon retrieved successfully: Code {}", code);
        return couponMapper.toDto(coupon);
    }

    @Override
    public boolean isCouponValid(Long couponId, Long productId) {
        LocalDate currentDate = LocalDate.now();

        try {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: ", "CouponId", couponId.toString()));

            if (currentDate.isBefore(coupon.getStartDate()) || currentDate.isAfter(coupon.getEndDate())) {
                return false;
            }

            if (coupon.getMaxRedemptions() != null && coupon.getRedeemCount() >= coupon.getMaxRedemptions()) {
                return false;
            }

            boolean isApplicable = coupon.getProducts().stream()
                    .anyMatch(product -> product.getId().equals(productId));

            return isApplicable;

        } catch (ResourceNotFoundException e) {
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean isCouponCodeAlreadyUsed(String code) {
        return couponRepository.existsByCode(code);
    }

    @Override
    public CouponDto applyCouponToProduct(Long couponId, Long productId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + couponId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: ", "ProductId", productId.toString()));

        if (LocalDate.now().isAfter(coupon.getEndDate())) {
            throw new CouponExpiredException("Coupon has expired and cannot be applied.");
        }

        if (coupon.getMaxRedemptions() != null && coupon.getRedeemCount() >= coupon.getMaxRedemptions()) {
            throw new CouponLimitReachedException("Coupon redemption limit has been reached.");
        }

        if (product.getCoupon() != null && product.getCoupon().getId().equals(couponId)) {
            throw new CouponAlreadyAppliedException("This coupon is already applied to the product.");
        }

        product.setCoupon(coupon);
        productRepository.save(product);

        coupon.setRedeemCount(coupon.getRedeemCount() + 1);
        couponRepository.save(coupon);

        logger.info("Coupon {} applied to product {}", couponId, productId);
        return couponMapper.toDto(coupon);
    }
}
