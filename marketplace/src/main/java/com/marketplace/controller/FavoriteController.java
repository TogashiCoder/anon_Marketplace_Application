package com.marketplace.controller;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.service.IFavoriteService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
public class FavoriteController {

    @Autowired
    private IFavoriteService favoriteService;



    @PostMapping
    public ResponseEntity<FavoriteDto> addFavorite(
            @RequestParam @NotNull Long buyerId,
            @RequestParam @NotNull Long productId) {

        FavoriteDto favoriteDto = favoriteService.addFavorite(buyerId, productId);
        return ResponseEntity.ok(favoriteDto);
    }

    @DeleteMapping
    public ResponseEntity<Void> removeFavorite(@RequestParam Long buyerId, @RequestParam Long productId) {
        favoriteService.removeFavoriteByBuyerAndProduct(buyerId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<FavoriteDto>> getFavoritesByBuyerId(@PathVariable Long buyerId) {
        List<FavoriteDto> favorites = favoriteService.getFavoritesByBuyerId(buyerId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/check")
    public ResponseEntity<Boolean> isProductFavorited(@RequestParam Long buyerId, @RequestParam Long productId) {
        boolean isFavorited = favoriteService.isProductFavorited(buyerId, productId);
        return ResponseEntity.ok(isFavorited);
    }
}
