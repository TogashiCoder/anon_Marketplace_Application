package com.marketplace.controller;


import com.marketplace.dto.FavoriteDto;
import com.marketplace.service.IFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;

    // Add a favorite
    @PostMapping
    public ResponseEntity<FavoriteDto> addFavorite(@RequestParam Long buyerId, @RequestParam Long productId) {
        FavoriteDto favoriteDto = favoriteService.addFavorite(buyerId, productId);
        return ResponseEntity.ok(favoriteDto);
    }

    // Remove a favorite using buyerId and productId
    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long buyerId, @RequestParam Long productId) {
        favoriteService.removeFavoriteByBuyerAndProduct(buyerId, productId);
        return ResponseEntity.noContent().build();
    }

    // Get favorites by buyer ID
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<FavoriteDto>> getFavoritesByBuyerId(@PathVariable Long buyerId) {
        List<FavoriteDto> favorites = favoriteService.getFavoritesByBuyerId(buyerId);
        return ResponseEntity.ok(favorites);
    }

    // Check if a product is favorited
    @GetMapping("/check")
    public ResponseEntity<Boolean> isProductFavorited(@RequestParam Long buyerId, @RequestParam Long productId) {
        boolean isFavorited = favoriteService.isProductFavorited(buyerId, productId);
        return ResponseEntity.ok(isFavorited);
    }
}

