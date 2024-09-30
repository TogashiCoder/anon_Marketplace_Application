package com.marketplace.controller;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.service.IFavoriteService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/favorites")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FavoriteController {


    private final IFavoriteService favoriteService;




    @PostMapping("/make/{buyerId}/{productId}")
    public ResponseEntity<FavoriteDto> addFavorite(
        @PathVariable @NotNull Long buyerId,
        @PathVariable @NotNull Long productId) {
    FavoriteDto favoriteDto = favoriteService.addFavorite(buyerId, productId);
    return ResponseEntity.ok(favoriteDto);
    }



    @DeleteMapping("/{buyerId}/{productId}")
        public ResponseEntity<Void> removeFavorite(@PathVariable Long buyerId, @PathVariable Long productId) {
        favoriteService.removeFavoriteByBuyerAndProduct(buyerId, productId);
        return ResponseEntity.noContent().build();
        }


    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<List<FavoriteDto>> getFavoritesByBuyerId(@PathVariable Long buyerId) {
        List<FavoriteDto> favorites = favoriteService.getFavoritesByBuyerId(buyerId);
        return ResponseEntity.ok(favorites);
    }



    @GetMapping("/check/{buyerId}/{productId}")
    public ResponseEntity<Boolean> isProductFavorited(@PathVariable Long buyerId, @PathVariable Long productId) {
        boolean isFavorited = favoriteService.isProductFavorited(buyerId, productId);
        return ResponseEntity.ok(isFavorited);
    }



}
