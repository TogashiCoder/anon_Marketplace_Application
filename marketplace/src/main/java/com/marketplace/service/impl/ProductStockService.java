package com.marketplace.service.impl;

import com.marketplace.dto.ProductStockDto;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.model.Product;
import com.marketplace.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductStockService {

    @Autowired
    private ProductRepository productRepository;

    public List<ProductStockDto> getProductStockBySellerId(Long sellerId) {
        List<Product> products = productRepository.findBySellerId(sellerId);

        if (products.isEmpty()) {
            throw new ResourceNotFoundException("Products", "sellerId", sellerId.toString());
        }

        return products.stream()
                .map(product -> new ProductStockDto(
                        product.getName(),
                        product.getStockQuantity(),
                        product.getPrice()
                ))
                .collect(Collectors.toList());
    }
}