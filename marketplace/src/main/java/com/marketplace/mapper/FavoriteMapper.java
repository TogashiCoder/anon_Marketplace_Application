package com.marketplace.mapper;
import com.marketplace.dto.FavoriteDto;
import com.marketplace.dto.ProductDto;
import com.marketplace.model.Favorite;
import com.marketplace.model.Image;
import com.marketplace.model.Product;
import com.marketplace.model.Video;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


@Component
public class FavoriteMapper {

    // Convert Favorite to FavoriteDto
    public FavoriteDto toDto(Favorite favorite) {
        if (favorite == null) {
            return null;
        }

        FavoriteDto dto = new FavoriteDto();
        dto.setId(favorite.getId());
        dto.setBuyerId(favorite.getBuyer() != null ? favorite.getBuyer().getId() : null);

        // Manually map Product to ProductDto
        Product product = favorite.getProduct();
        if (product != null) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setName(product.getName());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setStockQuantity(product.getStockQuantity());
            productDto.setMinimumOrderQuantity(product.getMinimumOrderQuantity());
            productDto.setDiscountPercentage(product.getDiscountPercentage());
            productDto.setRating(product.getRating());
            productDto.setSellerId(product.getSeller() != null ? product.getSeller().getId() : null);

            // Map image URLs from images list
            if (product.getImages() != null) {
                productDto.setImageUrls(product.getImages().stream()
                        .map(Image::getImageUrl)
                        .collect(Collectors.toList()));
            }

            // Map video URLs from videos list
            if (product.getVideos() != null) {
                productDto.setVideoUrls(product.getVideos().stream()
                        .map(Video::getVideoUrl)
                        .collect(Collectors.toList()));
            }

            productDto.setCreatedAt(product.getCreatedAt());
            productDto.setUpdatedAt(product.getUpdatedAt());
            productDto.setCategoryId(product.getCategory() != null ? product.getCategory().getId() : null);
            productDto.setCategoryName(product.getCategory() != null ? product.getCategory().getName() : null);
            dto.setProduct(productDto);
        }

        dto.setFavoritedAt(favorite.getFavoritedAt());
        return dto;
    }

    // Convert FavoriteDto to Favorite remains unchanged
}
