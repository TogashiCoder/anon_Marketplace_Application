package com.marketplace.service;

import com.marketplace.dto.ProductDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface IProductService {
    ResponseEntity<ProductDto> createProduct(ProductDto productDto, List<MultipartFile> images, List<MultipartFile> videos);
    ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto, List<MultipartFile> images, List<MultipartFile> videos);
    ResponseEntity<ProductDto> getProductById(Long id);
    ResponseEntity<List<ProductDto>> getAllProducts();
    ResponseEntity<String> deleteProduct(Long id);
    ResponseEntity<List<ProductDto>> getAllProductsByCategory(Long categoryId);
    ResponseEntity<List<ProductDto>> getAllProductsBySellerId(Long sellerId);
    ResponseEntity<List<ProductDto>> getAllProductsWithoutCoupon();

}

