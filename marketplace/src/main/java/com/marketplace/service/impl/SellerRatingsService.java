package com.marketplace.service.impl;
import com.marketplace.dto.SellerRatingsDTO;
import com.marketplace.model.Seller;
import com.marketplace.model.Product;
import com.marketplace.model.Rating;
import com.marketplace.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SellerRatingsService {

    @Autowired
    private SellerRepository sellerRepository;

    public SellerRatingsDTO getSellerRatings(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new RuntimeException("Seller not found"));

        SellerRatingsDTO dto = new SellerRatingsDTO();
        dto.setId(seller.getId());
//        dto.setName(seller.getFirstname() + " " + seller.getLastname());
        dto.setName(seller.getUsername());
        dto.setProfileImageUrl(seller.getProfileImage() != null ? seller.getProfileImage().getImageUrl() : null);

        List<Rating> allRatings = seller.getRatings();
        dto.setOverallRating(calculateOverallRating(allRatings));
        dto.setTotalReviews((long) allRatings.size());
        dto.setRatingDistribution(calculateRatingDistribution(allRatings));
        dto.setProductRatings(getProductRatings(seller.getProducts()));

        return dto;
    }

    private BigDecimal calculateOverallRating(List<Rating> ratings) {
        if (ratings.isEmpty()) {
            return BigDecimal.ZERO;
        }
        BigDecimal sum = ratings.stream()
                .map(Rating::getValue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(BigDecimal.valueOf(ratings.size()), 2, RoundingMode.HALF_UP);
    }

    private List<SellerRatingsDTO.RatingDistributionDTO> calculateRatingDistribution(List<Rating> ratings) {
        Map<Integer, Long> distributionMap = ratings.stream()
                .collect(Collectors.groupingBy(r -> r.getValue().intValue(), Collectors.counting()));

        List<SellerRatingsDTO.RatingDistributionDTO> distribution = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            SellerRatingsDTO.RatingDistributionDTO dto = new SellerRatingsDTO.RatingDistributionDTO();
            dto.setStars(i);
            long count = distributionMap.getOrDefault(i, 0L);
            dto.setPercentage((int) (count * 100 / ratings.size()));
            distribution.add(dto);
        }
        return distribution;
    }

    private List<SellerRatingsDTO.ProductRatingDTO> getProductRatings(List<Product> products) {
        return products.stream().map(product -> {
            SellerRatingsDTO.ProductRatingDTO dto = new SellerRatingsDTO.ProductRatingDTO();
            dto.setId(product.getId());
            dto.setName(product.getName());
            dto.setRating(product.getRating());
            dto.setReviews((long) product.getRatings().size());
            return dto;
        }).collect(Collectors.toList());
    }
}