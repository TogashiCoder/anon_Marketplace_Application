package com.marketplace.mapper;


import org.springframework.stereotype.Component;
import com.marketplace.dto.RatingDTO;
import com.marketplace.model.Rating;
import com.marketplace.model.Product;
import com.marketplace.model.Seller;

@Component
public class RatingMapper {

    public static RatingDTO toDTO(Rating rating) {
        if (rating == null) {
            return null;
        }
        return new RatingDTO(
                rating.getId(),
                rating.getValue(),
                rating.getProduct() != null ? rating.getProduct().getId() : null,
                rating.getSeller() != null ? rating.getSeller().getId() : null,
                rating.getCreatedAt()
        );
    }

    public static Rating toEntity(RatingDTO ratingDTO, Product product, Seller seller) {
        if (ratingDTO == null) {
            return null;
        }
        Rating rating = new Rating();
        rating.setId(ratingDTO.getId());
        rating.setValue(ratingDTO.getValue());
        if (product != null) {
            rating.setProduct(product);
        }
        if (seller != null) {
            rating.setSeller(seller);
        }
        rating.setCreatedAt(ratingDTO.getCreatedAt());
        return rating;
    }
}
