package com.marketplace.service.impl;

import com.marketplace.dto.ProductViewDTO;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.ProductViewMapper;
import com.marketplace.model.Product;
import com.marketplace.model.ProductView;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.ProductViewRepository;
import com.marketplace.service.IProductViews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class IProductViewsImpl implements IProductViews {

    @Autowired
    private ProductViewRepository productViewRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductViewMapper productViewMapper;



    @Override
    public ProductViewDTO addView(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("product","id",productId.toString()));

        ProductView view = new ProductView();
        view.setProduct(product);
        view.setViewedAt(LocalDateTime.now());
        view = productViewRepository.save(view);

        return productViewMapper.toDTO(view);
    }

    @Override
    public long getTotalViews(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product","id",productId.toString());
        }
        return productViewRepository.countByProductId(productId);
    }

    @Override
    public long getDailyViews(Long productId, LocalDate date) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product","id",productId.toString());
        }
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return productViewRepository.countByProductIdAndViewedAtBetween(productId, start, end);
    }


    @Override
    public long getWeeklyViews(Long productId, LocalDate weekStart) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product","id",productId.toString());
        }
        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end = weekStart.plusWeeks(1).atStartOfDay();
        return productViewRepository.countByProductIdAndViewedAtBetween(productId, start, end);
    }

    @Override
    public long getMonthlyViews(Long productId, LocalDate monthStart) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product","id",productId.toString());
        }
        LocalDateTime start = monthStart.atStartOfDay();
        LocalDateTime end = monthStart.plusMonths(1).atStartOfDay();
        return productViewRepository.countByProductIdAndViewedAtBetween(productId, start, end);
    }

    @Override
    public List<ProductViewDTO> getProductViews(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("product","id",productId.toString());
        }
        return productViewRepository.findByProductIdOrderByViewedAtDesc(productId)
                .stream()
                .map(productViewMapper::toDTO)
                .collect(Collectors.toList());
    }




    @Override
    public Map<String, List<Map<String, Object>>> getProductViewStatsForSeller(Long sellerId, LocalDate date) {
        List<Product> sellerProducts = productRepository.findBySellerId(sellerId);

        Map<String, List<Map<String, Object>>> result = new HashMap<>();

        result.put("daily", getStatsForPeriod(sellerProducts, date, PeriodType.DAILY));
        result.put("weekly", getStatsForPeriod(sellerProducts, date.minusDays(6), PeriodType.WEEKLY));
        result.put("monthly", getStatsForPeriod(sellerProducts, date.withDayOfMonth(1), PeriodType.MONTHLY));

        return result;
    }

    private List<Map<String, Object>> getStatsForPeriod(List<Product> products, LocalDate startDate, PeriodType periodType) {
        return products.stream().map(product -> {
            Long productId = product.getId();
            String productName = product.getName();
            long views;

            switch (periodType) {
                case DAILY:
                    views = getDailyViews(productId, startDate);
                    break;
                case WEEKLY:
                    views = getWeeklyViews(productId, startDate);
                    break;
                case MONTHLY:
                    views = getMonthlyViews(productId, startDate);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid period type");
            }

            Map<String, Object> stat = new HashMap<>();
            stat.put("name", productName);
            stat.put("value", views);
            return stat;
        }).collect(Collectors.toList());
    }

    private enum PeriodType {
        DAILY, WEEKLY, MONTHLY
    }



}
