package com.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.ProductDto;
import com.marketplace.service.IProductService;
import jakarta.validation.ConstraintViolation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private IProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);




    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestPart("product") String productJson,
            @RequestPart(value = "images", required = false) List<MultipartFile> images,
            @RequestPart(value = "videos", required = false) List<MultipartFile> videos) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ProductDto productDto = mapper.readValue(productJson, ProductDto.class);

            ResponseEntity<ProductDto> response = productService.createProduct(productDto, images, videos);
            return response;

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing product JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product: " + e.getMessage());
        }
    }






    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }



    @GetMapping("/all")
    public ResponseEntity<?> getAllProducts() {
        return productService.getAllProducts();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        return productService.deleteProduct(id);
    }




    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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





    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(@PathVariable Long id) {
        return productService.getAllProductsByCategory(id);
    }

    @GetMapping("/seller/{id}")
    public ResponseEntity<List<ProductDto>> getAllProductsBySellerId(@PathVariable Long id) {
        return productService.getAllProductsBySellerId(id);
    }



}