package com.marketplace.service;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.model.Favorite;

import java.util.List;

public interface IFavoriteService {

    FavoriteDto addFavorite(Long buyerId, Long productId);
    void removeFavorite(Long favoriteId);
    void removeFavoriteByBuyerAndProduct(Long buyerId, Long productId); // New method
    List<FavoriteDto> getFavoritesByBuyerId(Long buyerId);
    boolean isProductFavorited(Long buyerId, Long productId);
}

