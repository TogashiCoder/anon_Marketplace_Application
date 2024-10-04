package com.marketplace.service.impl;

import com.marketplace.dto.CouponDto;
import com.marketplace.exception.*;
import com.marketplace.mapper.CouponMapper;
import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.service.ICouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private final BuyerRepository buyerRepository;
    private final CouponMapper couponMapper;
    private final CouponUsageRepository couponUsageRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final CartItemRepository cartItemRepository;

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

//    @Override
//    public boolean isCouponValid(Long couponId, Long productId) {
//        LocalDate currentDate = LocalDate.now();
//
//        try {
//            Coupon coupon = couponRepository.findById(couponId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: ", "CouponId", couponId.toString()));
//
//            if (currentDate.isBefore(coupon.getStartDate()) || currentDate.isAfter(coupon.getEndDate())) {
//                return false;
//            }
//
//            if (coupon.getMaxRedemptions() != null && coupon.getRedeemCount() >= coupon.getMaxRedemptions()) {
//                return false;
//            }
//
//            boolean isApplicable = coupon.getProducts().stream()
//                    .anyMatch(product -> product.getId().equals(productId));
//
//            return isApplicable;
//
//        } catch (ResourceNotFoundException e) {
//            return false;
//        } catch (Exception e) {
//            return false;
//        }
//    }


    @Override
    public boolean isCouponValid(Long couponId, Long productId) {
        LocalDate currentDate = LocalDate.now();

        try {
            Coupon coupon = couponRepository.findById(couponId)
                    .orElseThrow(() -> new ResourceNotFoundException("Coupon not found with id: ", "CouponId", couponId.toString()));

            if (currentDate.isBefore(coupon.getStartDate()) || currentDate.isAfter(coupon.getEndDate())) {
                return false;
            }

            if (coupon.getMaxRedemptions() != null) {
                long usageCount = couponUsageRepository.countByCouponId(couponId);
                if (usageCount >= coupon.getMaxRedemptions()) {
                    return false;
                }
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




//    @Override
//    public CouponDto applyCouponToProduct(Long couponId, Long productId) {
//        Coupon coupon = couponRepository.findById(couponId)
//                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + couponId));
//
//        Product product = productRepository.findById(productId)
//                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: ", "ProductId", productId.toString()));
//
//        if (LocalDate.now().isAfter(coupon.getEndDate())) {
//            throw new CouponExpiredException("Coupon has expired and cannot be applied.");
//        }
//
//        if (coupon.getMaxRedemptions() != null && coupon.getRedeemCount() >= coupon.getMaxRedemptions()) {
//            throw new CouponLimitReachedException("Coupon redemption limit has been reached.");
//        }
//
//        if (product.getCoupon() != null && product.getCoupon().getId().equals(couponId)) {
//            throw new CouponAlreadyAppliedException("This coupon is already applied to the product.");
//        }
//
//        product.setCoupon(coupon);
//        productRepository.save(product);
//
//        coupon.setRedeemCount(coupon.getRedeemCount() + 1);
//        couponRepository.save(coupon);
//
//        logger.info("Coupon {} applied to product {}", couponId, productId);
//        return couponMapper.toDto(coupon);
//    }


    @Transactional
    @Override
    public CouponDto applyCouponToProduct(Long couponId, Long productId, Long buyerId) {
        Coupon coupon = couponRepository.findById(couponId)
                .orElseThrow(() -> new CouponNotFoundException("Coupon not found with id: " + couponId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: ", "ProductId", productId.toString()));

        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer not found with id: ", "BuyerId", buyerId.toString()));

        // Validate coupon
        validateCoupon(coupon, product, buyer);

        // Get buyer's shopping cart
        ShoppingCart shoppingCart = shoppingCartRepository.findByBuyerAndIsActiveTrue(buyer)
                .orElseThrow(() -> new ResourceNotFoundException("Active shopping cart not found for buyer: ", "BuyerId", buyerId.toString()));

        // Find the cart item for this product
        CartItem cartItem = shoppingCart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Product not found in cart", "ProductId", productId.toString()));

        // Apply coupon to product
        product.setCoupon(coupon);
        productRepository.save(product);

        // Calculate and set discounted price
        BigDecimal discountMultiplier = BigDecimal.ONE.subtract(
                BigDecimal.valueOf(coupon.getDiscountPercentage()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
        );
        BigDecimal discountedPrice = product.getPrice().multiply(discountMultiplier).setScale(2, RoundingMode.HALF_UP);

        // Update cart item price
        cartItem.setPrice(discountedPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        cartItemRepository.save(cartItem);

        // Create and save coupon usage
        CouponUsage couponUsage = new CouponUsage();
        couponUsage.setCoupon(coupon);
        couponUsage.setProduct(product);
        couponUsage.setBuyer(buyer);
        couponUsage.setUsed(true);
        couponUsageRepository.save(couponUsage);

        // Increment redeem count
        coupon.setRedeemCount(coupon.getRedeemCount() + 1);
        Coupon updatedCoupon = couponRepository.save(coupon);

        logger.info("Coupon {} applied to product {} for buyer {}. New redeem count: {}",
                couponId, productId, buyerId, updatedCoupon.getRedeemCount());

        return couponMapper.toDto(updatedCoupon);
    }

    private void validateCoupon(Coupon coupon, Product product, Buyer buyer) {
        LocalDate currentDate = LocalDate.now();

        if (currentDate.isBefore(coupon.getStartDate()) || currentDate.isAfter(coupon.getEndDate())) {
            throw new CouponExpiredException("Coupon has expired or not yet active");
        }

        long usageCount = couponUsageRepository.countByCouponId(coupon.getId());
        if (coupon.getMaxRedemptions() != null && usageCount >= coupon.getMaxRedemptions()) {
            throw new CouponLimitReachedException("Coupon redemption limit has been reached");
        }

        // Check if buyer has already used this coupon for this product
        boolean alreadyUsed = couponUsageRepository.existsByCouponAndBuyerAndProduct(coupon, buyer, product);
        if (alreadyUsed) {
            throw new CouponAlreadyUsedException("You have already used this coupon for this product");
        }

        if (product.getCoupon() != null && product.getCoupon().getId().equals(coupon.getId())) {
            throw new CouponAlreadyAppliedException("This coupon is already applied to the product");
        }
    }

}
