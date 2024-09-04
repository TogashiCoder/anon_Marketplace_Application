package com.marketplace.service.impl;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.FavoriteMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Favorite;
import com.marketplace.model.Product;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.FavoriteRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.IFavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IFavoriteServiceImpl implements IFavoriteService {

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    private final FavoriteMapper favoriteMapper = FavoriteMapper.INSTANCE;

    @Override
    public FavoriteDto addFavorite(Long buyerId, Long productId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "ID", buyerId.toString()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId.toString()));

        Favorite favorite = new Favorite();
        favorite.setBuyer(buyer);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);

        return favoriteMapper.toDto(favorite);
    }

    @Override
    public void removeFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "ID", favoriteId.toString()));
        favoriteRepository.delete(favorite);
    }

    @Override
    public void removeFavoriteByBuyerAndProduct(Long buyerId, Long productId) {
        Favorite favorite = favoriteRepository.findByBuyerIdAndProductId(buyerId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "buyerId and productId", buyerId + " and " + productId));
        favoriteRepository.delete(favorite);
    }

    @Override
    public List<FavoriteDto> getFavoritesByBuyerId(Long buyerId) {
        List<Favorite> favorites = favoriteRepository.findByBuyerId(buyerId);
        return favorites.stream()
                .map(favoriteMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProductFavorited(Long buyerId, Long productId) {
        return favoriteRepository.existsByBuyerIdAndProductId(buyerId, productId);
    }
}
