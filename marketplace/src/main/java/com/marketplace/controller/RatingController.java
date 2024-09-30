package com.marketplace.controller;

import com.marketplace.dto.ProductDto;
import com.marketplace.dto.RatingDTO;
import com.marketplace.service.IRatingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@RequiredArgsConstructor
public class RatingController {

    private final IRatingService ratingService;

    @PostMapping
    public ResponseEntity<RatingDTO> createRating(@Valid @RequestBody RatingDTO ratingDTO) {
        RatingDTO createdRating = ratingService.createRating(ratingDTO);
        return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingDTO> getRating(@PathVariable Long id) {
        RatingDTO rating = ratingService.getRating(id);
        return ResponseEntity.ok(rating);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long id) {
        ratingService.deleteRating(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<RatingDTO>> getAllRatingsByProduct(@PathVariable Long productId) {
        List<RatingDTO> ratings = ratingService.getAllRatingsByProduct(productId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<RatingDTO>> getAllRatingsBySeller(@PathVariable Long sellerId) {
        List<RatingDTO> ratings = ratingService.getAllRatingsBySeller(sellerId);
        return ResponseEntity.ok(ratings);
    }

    @GetMapping("/count/product/{productId}")
    public ResponseEntity<Long> getNumberOfRatingsForProduct(@PathVariable Long productId) {
        Long ratingCount = ratingService.getNumberOfRatingsForProduct(productId);
        return ResponseEntity.ok(ratingCount);
    }

    @GetMapping("/count/seller/{sellerId}")
    public ResponseEntity<Long> getNumberOfRatingsForSeller(@PathVariable Long sellerId) {
        Long ratingCount = ratingService.getNumberOfRatingsForSeller(sellerId);
        return ResponseEntity.ok(ratingCount);
    }


    @GetMapping("/average/seller/{sellerId}")
    public ResponseEntity<BigDecimal> getAverageRatingForSellerProducts(@PathVariable Long sellerId) {
        BigDecimal averageRating = ratingService.getAverageRatingForSellerProducts(sellerId);
        return ResponseEntity.ok(averageRating);
    }

    @GetMapping("/best-rated")
    public ResponseEntity<List<ProductDto>> getBestRatedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductDto> bestRatedProducts = ratingService.getBestRatedProducts(limit);
        return ResponseEntity.ok(bestRatedProducts);
    }

    @GetMapping("/most-viewed")
    public ResponseEntity<List<ProductDto>> getMostViewedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductDto> mostViewedProducts = ratingService.getMostViewedProducts(limit);
        return ResponseEntity.ok(mostViewedProducts);
    }

    @GetMapping("/most-favorited")
    public ResponseEntity<List<ProductDto>> getMostFavoritedProducts(@RequestParam(defaultValue = "10") int limit) {
        List<ProductDto> mostFavoritedProducts = ratingService.getMostFavoritedProducts(limit);
        return ResponseEntity.ok(mostFavoritedProducts);
    }

}