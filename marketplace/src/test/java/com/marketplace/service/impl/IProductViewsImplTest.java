package com.marketplace.service.impl;

import com.marketplace.dto.ProductViewDTO;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.ProductViewMapper;
import com.marketplace.model.Product;
import com.marketplace.model.ProductView;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.ProductViewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IProductViewsImplTest {

    @Mock
    private ProductViewRepository productViewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductViewMapper productViewMapper;

    @InjectMocks
    private IProductViewsImpl productViewsService;

    private Product product;
    private ProductView productView;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        productView = new ProductView();
        productView.setProduct(product);
        productView.setViewedAt(LocalDateTime.now());
    }

    @Test
    public void testAddView_Success() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(productViewRepository.save(any(ProductView.class))).thenReturn(productView);

        ProductViewDTO productViewDTO = new ProductViewDTO();
        when(productViewMapper.toDTO(productView)).thenReturn(productViewDTO);

        // Act
        ProductViewDTO result = productViewsService.addView(1L);

        // Assert
        assertNotNull(result);
        verify(productRepository, times(1)).findById(1L);
        verify(productViewRepository, times(1)).save(any(ProductView.class));
    }

    @Test
    public void testAddView_ProductNotFound() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productViewsService.addView(1L));
        verify(productRepository, times(1)).findById(1L);
        verify(productViewRepository, never()).save(any(ProductView.class));
    }

    @Test
    public void testGetTotalViews_Success() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(true);
        when(productViewRepository.countByProductId(1L)).thenReturn(10L);

        // Act
        long totalViews = productViewsService.getTotalViews(1L);

        // Assert
        assertEquals(10L, totalViews);
        verify(productRepository, times(1)).existsById(1L);
        verify(productViewRepository, times(1)).countByProductId(1L);
    }

    @Test
    public void testGetTotalViews_ProductNotFound() {
        // Arrange
        when(productRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> productViewsService.getTotalViews(1L));
        verify(productRepository, times(1)).existsById(1L);
        verify(productViewRepository, never()).countByProductId(anyLong());
    }

    @Test
    public void testGetDailyViews_Success() {
        // Arrange
        LocalDate date = LocalDate.now();
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productViewRepository.countByProductIdAndViewedAtBetween(1L, start, end)).thenReturn(5L);

        // Act
        long dailyViews = productViewsService.getDailyViews(1L, date);

        // Assert
        assertEquals(5L, dailyViews);
        verify(productRepository, times(1)).existsById(1L);
        verify(productViewRepository, times(1)).countByProductIdAndViewedAtBetween(1L, start, end);
    }

    @Test
    public void testGetWeeklyViews_Success() {
        // Arrange
        LocalDate weekStart = LocalDate.now().minusDays(7);
        LocalDateTime start = weekStart.atStartOfDay();
        LocalDateTime end = weekStart.plusWeeks(1).atStartOfDay();

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productViewRepository.countByProductIdAndViewedAtBetween(1L, start, end)).thenReturn(20L);

        // Act
        long weeklyViews = productViewsService.getWeeklyViews(1L, weekStart);

        // Assert
        assertEquals(20L, weeklyViews);
        verify(productRepository, times(1)).existsById(1L);
        verify(productViewRepository, times(1)).countByProductIdAndViewedAtBetween(1L, start, end);
    }


}