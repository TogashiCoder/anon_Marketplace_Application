package com.marketplace.service.impl;

import com.marketplace.dto.CouponUsageDto;
import com.marketplace.exception.*;
import com.marketplace.mapper.CouponUsageMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Coupon;
import com.marketplace.model.CouponUsage;
import com.marketplace.model.Product;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.CouponRepository;
import com.marketplace.repository.CouponUsageRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.ICouponUsageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ICouponUsageServiceImpl implements ICouponUsageService {

    private static final Logger logger = LoggerFactory.getLogger(ICouponUsageServiceImpl.class);

    private final CouponUsageRepository couponUsageRepository;
    private final CouponRepository couponRepository;
    private final BuyerRepository buyerRepository;
    private final ProductRepository productRepository;
    private final CouponUsageMapper couponUsageMapper;

    @Override
    public CouponUsageDto createCouponUsage(CouponUsageDto couponUsageDto) {
        try {
            couponUsageDto.setUsed(false); // Set 'used' to false by default when creating
            CouponUsage couponUsage = couponUsageMapper.toEntity(couponUsageDto);
            CouponUsage savedCouponUsage = couponUsageRepository.save(couponUsage);
            logger.info("Coupon usage created successfully: {}", savedCouponUsage.getId());
            return couponUsageMapper.toDto(savedCouponUsage);
        } catch (Exception e) {
            logger.error("Error creating coupon usage", e);
            throw new CouponCreationException("Failed to create coupon usage.");
        }
    }

    @Override
    public CouponUsageDto getCouponUsageById(Long id) {
        return couponUsageRepository.findById(id)
                .map(couponUsageMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Coupon usage not found with id: ", "id", id.toString()));
    }

    @Override
    public List<CouponUsageDto> getAllCouponUsages() {
        List<CouponUsage> couponUsages = couponUsageRepository.findAll();
        return couponUsages.stream()
                .map(couponUsageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponUsageDto> getCouponUsagesByCouponId(Long couponId) {
        return couponUsageRepository.findByCouponId(couponId).stream()
                .map(couponUsageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CouponUsageDto> getCouponUsagesByBuyerId(Long buyerId) {
        return couponUsageRepository.findByBuyerId(buyerId).stream()
                .map(couponUsageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isCouponUsedByBuyer(Long couponId, Long buyerId) {
        return couponUsageRepository.existsByCouponIdAndBuyerIdAndUsed(couponId, buyerId, true);
    }

    @Override
    public void markCouponAsUsed(Long couponId, Long buyerId, Long productId) {
        try {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + couponId));
            Buyer buyer = buyerRepository.findById(buyerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: ", "id", buyerId.toString()));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: ", "id", productId.toString()));

            if (isCouponUsedByBuyer(couponId, buyerId)) {
                throw new CouponAlreadyUsedException("Coupon has already been used by this buyer.");
            }

            // Mark the coupon as used
            CouponUsage couponUsage = new CouponUsage();
            couponUsage.setCoupon(coupon);
            couponUsage.setBuyer(buyer);
            couponUsage.setProduct(product);
            couponUsage.setUsed(true);
            couponUsageRepository.save(couponUsage);

            // Increment redeem count of the coupon
            coupon.setRedeemCount(coupon.getRedeemCount() + 1);
            couponRepository.save(coupon);

            logger.info("Coupon {} marked as used by buyer {} for product {}", couponId, buyerId, productId);
        } catch (CouponAlreadyUsedException e) {
            logger.error("Coupon already used by buyer", e);
            throw e;
        } catch (Exception e) {
            logger.error("Error marking coupon as used", e);
            throw new InvalidCouponException("Failed to mark coupon as used.");
        }
    }

    @Override
    public void deleteCouponUsage(Long id) {
        if (!couponUsageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Coupon usage not found with id: ", "id", id.toString());
        }
        couponUsageRepository.deleteById(id);
        logger.info("Coupon usage deleted successfully: ID {}", id);
    }
}
