package com.marketplace.service;

import com.marketplace.dto.ProductViewDTO;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface IProductViews {
    ProductViewDTO addView(Long productId);
    long getTotalViews(Long productId);
    long getDailyViews(Long productId, LocalDate date);
    long getWeeklyViews(Long productId, LocalDate weekStart);
    long getMonthlyViews(Long productId, LocalDate monthStart);
    List<ProductViewDTO> getProductViews(Long productId);

    // Updated method for fetching product view statistics for a specific seller
    Map<String, List<Map<String, Object>>> getProductViewStatsForSeller(Long sellerId, LocalDate date);

}
