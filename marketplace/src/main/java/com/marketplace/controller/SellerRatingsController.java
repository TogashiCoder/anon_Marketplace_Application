package com.marketplace.controller;

import com.marketplace.dto.SellerRatingsDTO;

import com.marketplace.service.impl.SellerRatingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/display/rating")
public class SellerRatingsController {

    @Autowired
    private SellerRatingsService sellerRatingsService;

    @GetMapping("/{sellerId}/ratings")
    public ResponseEntity<SellerRatingsDTO> getSellerRatings(@PathVariable Long sellerId) {
        SellerRatingsDTO ratings = sellerRatingsService.getSellerRatings(sellerId);
        return ResponseEntity.ok(ratings);
    }
}
