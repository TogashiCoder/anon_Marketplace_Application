package com.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.constants.ProductConstants;
import com.marketplace.dto.ProductDto;
import com.marketplace.service.IProductService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductDto productDto = mapper.readValue(productJson, ProductDto.class);

            // Create the product and get the response DTO
            ResponseEntity<ProductDto> response = productService.createProduct(productDto, images, videos);
            return response;

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing product JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductDto productDto = mapper.readValue(productJson, ProductDto.class);

            ResponseEntity<ProductDto> response = productService.updateProduct(id, productDto, images, videos);
            return response;

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing product JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating product: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }
}