package com.marketplace.controller;

import com.marketplace.dto.ProductViewDTO;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.service.IProductViews;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/productsV")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProductViewController {

    private final IProductViews productViewsService;

    @PostMapping("/{productId}/view")
    public ResponseEntity<ProductViewDTO> addView(@PathVariable Long productId) {
        try {
            ProductViewDTO view = productViewsService.addView(productId);
            return ResponseEntity.ok(view);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/views/total")
    public ResponseEntity<Long> getTotalViews(@PathVariable Long productId) {
        try {
            long totalViews = productViewsService.getTotalViews(productId);
            return ResponseEntity.ok(totalViews);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/views/daily")
    public ResponseEntity<Long> getDailyViews(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            long dailyViews = productViewsService.getDailyViews(productId, date);
            return ResponseEntity.ok(dailyViews);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/views/weekly")
    public ResponseEntity<Long> getWeeklyViews(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart) {
        try {
            long weeklyViews = productViewsService.getWeeklyViews(productId, weekStart);
            return ResponseEntity.ok(weeklyViews);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/views/monthly")
    public ResponseEntity<Long> getMonthlyViews(
            @PathVariable Long productId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate monthStart) {
        try {
            long monthlyViews = productViewsService.getMonthlyViews(productId, monthStart);
            return ResponseEntity.ok(monthlyViews);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{productId}/views")
    public ResponseEntity<List<ProductViewDTO>> getProductViews(@PathVariable Long productId) {
        try {
            List<ProductViewDTO> views = productViewsService.getProductViews(productId);
            return ResponseEntity.ok(views);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


}
