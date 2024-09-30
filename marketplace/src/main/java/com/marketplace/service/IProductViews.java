package com.marketplace.service;

import com.marketplace.dto.ProductViewDTO;
import java.time.LocalDate;
import java.util.List;

public interface IProductViews {
    ProductViewDTO addView(Long productId);
    long getTotalViews(Long productId);
    long getDailyViews(Long productId, LocalDate date);
    long getWeeklyViews(Long productId, LocalDate weekStart);
    long getMonthlyViews(Long productId, LocalDate monthStart);
    List<ProductViewDTO> getProductViews(Long productId);
}
