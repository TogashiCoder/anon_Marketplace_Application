package com.marketplace.service.impl;

import com.marketplace.dto.RatingDTO;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.RatingMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Product;
import com.marketplace.model.Rating;
import com.marketplace.model.Seller;
import com.marketplace.repository.*;
import com.marketplace.service.impl.IRatingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IRatingServiceImplTest {


    @InjectMocks
    private IRatingServiceImpl ratingService;

    @Mock
    private RatingRepository ratingRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private SellerRepository sellerRepository;
    @Mock
    private BuyerRepository buyerRepository;
    @Mock
    private RatingMapper ratingMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void testGetRating_success() {
        // Given
        Rating rating = new Rating();
        when(ratingRepository.findById(1L)).thenReturn(Optional.of(rating));

        // When
        RatingDTO ratingDTO = ratingService.getRating(1L);

        // Then
        assertNotNull(ratingDTO);
        verify(ratingRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRating_notFound() {
        // Given
        when(ratingRepository.findById(1L)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(ResourceNotFoundException.class, () -> ratingService.getRating(1L));
    }

    @Test
    void testDeleteRating_success() {
        // Given
        when(ratingRepository.existsById(1L)).thenReturn(true);

        // When
        ratingService.deleteRating(1L);

        // Then
        verify(ratingRepository, times(1)).deleteById(1L);
    }

    @Test
    void testGetAverageRatingForSellerProducts() {
        // Given
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(new Seller()));
        when(productRepository.findBySellerId(1L)).thenReturn(List.of(new Product()));

        // When
        BigDecimal avgRating = ratingService.getAverageRatingForSellerProducts(1L);

        // Then
        assertNotNull(avgRating);
        assertEquals(BigDecimal.ZERO, avgRating);
    }

    @Test
    void testCanRateProduct() {
        // Given
        when(ratingRepository.findByProductIdAndBuyerId(1L, 1L)).thenReturn(Optional.empty());

        // When
        boolean canRate = ratingService.canRateProduct(1L, 1L);

        // Then
        assertTrue(canRate);
    }

}