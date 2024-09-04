package com.marketplace.service.impl;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.dto.ProductDto;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.exception.AccessDeniedException; // Import custom exceptions if needed
import com.marketplace.mapper.FavoriteMapper;
import com.marketplace.model.*;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.FavoriteRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.service.IFavoriteService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IFavoriteServiceImpl implements IFavoriteService {

    private static final Logger logger = LoggerFactory.getLogger(IFavoriteServiceImpl.class);

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BuyerRepository buyerRepository;

    private final FavoriteMapper favoriteMapper;

    @Override
    public FavoriteDto addFavorite(Long buyerId, Long productId) {
        Buyer buyer = buyerRepository.findById(buyerId)
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "ID", buyerId.toString()));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "ID", productId.toString()));

        if (favoriteRepository.existsByBuyerIdAndProductId(buyerId, productId)) {
            logger.warn("Favorite already exists for buyerId: {} and productId: {}", buyerId, productId);
            throw new AccessDeniedException("Product is already favorited by the buyer.");
        }

        Favorite favorite = new Favorite();
        favorite.setBuyer(buyer);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);

        logger.info("Favorite added for buyerId: {} and productId: {}", buyerId, productId);
        return favoriteMapper.toDto(favorite);
    }

    @Override
    public void removeFavorite(Long favoriteId) {
        Favorite favorite = favoriteRepository.findById(favoriteId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "ID", favoriteId.toString()));

        favoriteRepository.delete(favorite);
        logger.info("Favorite removed with ID: {}", favoriteId);
    }

    @Override
    public void removeFavoriteByBuyerAndProduct(Long buyerId, Long productId) {
        Favorite favorite = favoriteRepository.findByBuyerIdAndProductId(buyerId, productId)
                .orElseThrow(() -> new ResourceNotFoundException("Favorite", "buyerId and productId", buyerId + " and " + productId));

        favoriteRepository.delete(favorite);
        logger.info("Favorite removed for buyerId: {} and productId: {}", buyerId, productId);
    }

    @Override
    public List<FavoriteDto> getFavoritesByBuyerId(Long buyerId) {
        List<Favorite> favorites = favoriteRepository.findByBuyerId(buyerId);

        return favorites.stream()
                .map(favorite -> {
                    FavoriteDto favoriteDto = new FavoriteDto();
                    favoriteDto.setId(favorite.getId());
                    favoriteDto.setBuyerId(favorite.getBuyer().getId());
                    favoriteDto.setFavoritedAt(favorite.getFavoritedAt());

                    Product product = favorite.getProduct();
                    if (product != null) {
                        ProductDto productDto = new ProductDto();
                        productDto.setId(product.getId());
                        productDto.setName(product.getName());
                        productDto.setDescription(product.getDescription());
                        productDto.setPrice(product.getPrice());
                        productDto.setMinimumOrderQuantity(product.getMinimumOrderQuantity());
                        productDto.setSellerId(product.getSeller() != null ? product.getSeller().getId() : null);
                        productDto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
                        productDto.setCreatedAt(product.getCreatedAt());
                        productDto.setUpdatedAt(product.getUpdatedAt());

                        List<String> imageUrls = product.getImages().stream()
                                .map(Image::getImageUrl)
                                .collect(Collectors.toList());
                        productDto.setImageUrls(imageUrls);

                        List<String> videoUrls = product.getVideos().stream()
                                .map(Video::getVideoUrl)
                                .collect(Collectors.toList());
                        productDto.setVideoUrls(videoUrls);

                        favoriteDto.setProduct(productDto);
                    }

                    return favoriteDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public boolean isProductFavorited(Long buyerId, Long productId) {
        return favoriteRepository.existsByBuyerIdAndProductId(buyerId, productId);
    }
}
