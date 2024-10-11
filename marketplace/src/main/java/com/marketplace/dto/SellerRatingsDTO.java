package com.marketplace.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class SellerRatingsDTO {
    private Long id;
    private String name;
    private String profileImageUrl;
    private BigDecimal overallRating;
    private Long totalReviews;
    private List<RatingDistributionDTO> ratingDistribution;
    private List<ProductRatingDTO> productRatings;

    @Data
    public static class RatingDistributionDTO {
        private int stars;
        private int percentage;
    }

    @Data
    public static class ProductRatingDTO {
        private Long id;
        private String name;
        private BigDecimal rating;
        private Long reviews;
    }
}
