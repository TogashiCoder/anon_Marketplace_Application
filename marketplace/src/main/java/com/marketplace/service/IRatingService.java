package com.marketplace.service;

import com.marketplace.dto.ProductDto;
import com.marketplace.dto.RatingDTO;
import com.marketplace.model.Rating;

import java.math.BigDecimal;
import java.util.List;

public interface IRatingService {
    RatingDTO createRating(RatingDTO ratingDTO);
    RatingDTO getRating(Long id);
    void deleteRating(Long id);
    List<RatingDTO> getAllRatingsByProduct(Long productId);
    List<RatingDTO> getAllRatingsBySeller(Long sellerId);
    Long getNumberOfRatingsForProduct(Long productId);
    Long getNumberOfRatingsForSeller(Long sellerId);
    BigDecimal getAverageRatingForSellerProducts(Long sellerId);
    List<ProductDto> getBestRatedProducts(int limit);
    List<ProductDto> getMostViewedProducts(int limit);
    List<ProductDto> getMostFavoritedProducts(int limit);

}
