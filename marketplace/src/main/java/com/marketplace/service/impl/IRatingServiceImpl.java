package com.marketplace.service.impl;

import com.marketplace.dto.ProductDto;
import com.marketplace.dto.RatingDTO;
import com.marketplace.dto.ReviewDto;
import com.marketplace.dto.SellerDto;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.exception.ReviewNotFoundException;
import com.marketplace.mapper.ProductMapper;
import com.marketplace.mapper.RatingMapper;
import com.marketplace.mapper.SellerMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Product;
import com.marketplace.model.Rating;
import com.marketplace.model.Seller;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.RatingRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.service.IProductService;
import com.marketplace.service.IRatingService;
import com.marketplace.service.ISellerService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class IRatingServiceImpl implements IRatingService {

    private final RatingRepository ratingRepository;
    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final RatingMapper ratingMapper;
    private final ProductMapper productMapper;

    @Override
    @Transactional
    public RatingDTO createRating(RatingDTO ratingDTO) {
        Product product = productRepository.findById(ratingDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", ratingDTO.getProductId().toString()));

        Seller seller = sellerRepository.findById(ratingDTO.getSellerId())
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", ratingDTO.getSellerId().toString()));

        Buyer buyer = buyerRepository.findById(ratingDTO.getBuyerId())
                .orElseThrow(() -> new ResourceNotFoundException("Buyer", "id", ratingDTO.getBuyerId().toString()));

        Rating rating = RatingMapper.toEntity(ratingDTO, product, seller,buyer);
        Rating savedRating = ratingRepository.save(rating);
        return RatingMapper.toDTO(savedRating);
    }

    @Override
    public RatingDTO getRating(Long id) {
        Rating rating = ratingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rating", "id", id.toString()));
        return RatingMapper.toDTO(rating);
    }

    @Override
    @Transactional
    public void deleteRating(Long id) {
        if (!ratingRepository.existsById(id)) {
            throw new ResourceNotFoundException("Rating", "id", id.toString());
        }
        ratingRepository.deleteById(id);
    }

    @Override
    public List<RatingDTO> getAllRatingsByProduct(Long productId) {
        List<Rating> ratings = ratingRepository.findAllByProductId(productId);
        return ratings.stream()
                .map(RatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<RatingDTO> getAllRatingsBySeller(Long sellerId) {
        List<Rating> ratings = ratingRepository.findAllBySellerId(sellerId);
        return ratings.stream()
                .map(RatingMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Long getNumberOfRatingsForProduct(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product", "id", productId.toString());
        }
        return ratingRepository.countByProductId(productId);
    }

    @Override
    public Long getNumberOfRatingsForSeller(Long sellerId) {
        if (!sellerRepository.existsById(sellerId)) {
            throw new ResourceNotFoundException("Seller", "id", sellerId.toString());
        }
        return ratingRepository.countBySellerId(sellerId);
    }



    @Override
    public BigDecimal getAverageRatingForSellerProducts(Long sellerId) {
        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() -> new ResourceNotFoundException("Seller", "id", sellerId.toString()));

        List<Product> sellerProducts = productRepository.findBySellerId(sellerId);

        if (sellerProducts.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalRating = BigDecimal.ZERO;
        int totalRatedProducts = 0;

        for (Product product : sellerProducts) {
            List<Rating> productRatings = ratingRepository.findAllByProductId(product.getId());
            if (!productRatings.isEmpty()) {
                BigDecimal productAvgRating = productRatings.stream()
                        .map(Rating::getValue)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(productRatings.size()), 2, RoundingMode.HALF_UP);
                totalRating = totalRating.add(productAvgRating);
                totalRatedProducts++;
            }
        }

        if (totalRatedProducts == 0) {
            return BigDecimal.ZERO;
        }

        return totalRating.divide(BigDecimal.valueOf(totalRatedProducts), 2, RoundingMode.HALF_UP);
    }




    @Override
    public List<ProductDto> getBestRatedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by("rating").descending());
        List<Product> bestRatedProducts = productRepository.findAll(pageable).getContent();
        return bestRatedProducts.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getMostViewedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> mostViewedProducts = productRepository.findMostViewedProducts(pageable);
        return mostViewedProducts.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getMostFavoritedProducts(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        List<Product> mostFavoritedProducts = productRepository.findMostFavoritedProducts(pageable);
        return mostFavoritedProducts.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
    }



    @Override
    public boolean canRateProduct(Long productId, Long buyerId) {
        Optional<Rating> existingRating = ratingRepository.findByProductIdAndBuyerId(productId, buyerId);
        return existingRating.isEmpty();
    }



    @Override
    public List<ReviewDto> getReviewsForProduct(Long productId) {
        List<ReviewDto> reviewDtos = new ArrayList<>();
        try {
            List<Rating> ratings = ratingRepository.findAllByProductId(productId);

            if (ratings.isEmpty()) {
                throw new ReviewNotFoundException("No reviews found for product with ID: " + productId);
            }

            for (Rating rating : ratings) {
                ReviewDto reviewDto = new ReviewDto();
                reviewDto.setProductName(rating.getProduct().getName());
                reviewDto.setBuyerName(rating.getBuyer().getFirstname() + " " + rating.getBuyer().getLastname());
                reviewDto.setBuyerProfilImageUrl(rating.getBuyer().getProfileImage().getImageUrl());
                reviewDto.setValue(rating.getValue());
                reviewDto.setComment(rating.getComment());
                reviewDto.setCreatedAt(rating.getCreatedAt());

                reviewDtos.add(reviewDto);
            }

        } catch (ReviewNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new ReviewNotFoundException("An error occurred while fetching reviews for product with ID: " + productId, e);
        }
        return reviewDtos;
    }



}
