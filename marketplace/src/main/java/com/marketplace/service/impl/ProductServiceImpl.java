package com.marketplace.service.impl;

import com.marketplace.controller.ProductController;
import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.dto.ProductDto;
import com.marketplace.exception.FuncErrorException;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.ProductMapper;
import com.marketplace.model.*;
import com.marketplace.repository.CategoryRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.service.IProductService;
import com.marketplace.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Override
    @Transactional
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto, List<MultipartFile> images, List<MultipartFile> videos) {
        // Convert DTO to entity
        Product product = productMapper.toEntity(productDto);

        // Fetch the Category entity using categoryId
        var category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productDto.getCategoryId()));
        product.setCategory(category);

        // Handle image uploads
        if (images != null && !images.isEmpty()) {
            List<Image> productImages = images.stream()
                    .map(image -> {
                        FileUploadUtil.assertAllowed(image, FileUploadUtil.IMAGE_PATTERN);
                        String fileName = FileUploadUtil.getFileName(
                                image.getOriginalFilename() != null ? image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf('.')) : "image",
                                image.getOriginalFilename() != null ? image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.') + 1) : "jpg"
                        );
                        CloudinaryResponse response = cloudinaryService.uploadFile(image, fileName, "image");
                        Image productImage = new Image();
                        productImage.setImageUrl(response.getUrl());
                        productImage.setCloudinaryImageId(response.getPublicId());
                        productImage.setProduct(product);
                        return productImage;
                    })
                    .collect(Collectors.toList());
            product.getImages().addAll(productImages);
        }

        // Handle video uploads
        if (videos != null && !videos.isEmpty()) {
            List<Video> productVideos = videos.stream()
                    .map(video -> {
                        FileUploadUtil.assertAllowed(video, FileUploadUtil.VIDEO_PATTERN);
                        String fileName = FileUploadUtil.getFileName(
                                video.getOriginalFilename() != null ? video.getOriginalFilename().substring(0, video.getOriginalFilename().lastIndexOf('.')) : "video",
                                video.getOriginalFilename() != null ? video.getOriginalFilename().substring(video.getOriginalFilename().lastIndexOf('.') + 1) : "mp4"
                        );
                        CloudinaryResponse response = cloudinaryService.uploadFile(video, fileName, "video");
                        Video productVideo = new Video();
                        productVideo.setVideoUrl(response.getUrl());
                        productVideo.setCloudinaryVideoId(response.getPublicId());
                        productVideo.setProduct(product);
                        return productVideo;
                    })
                    .collect(Collectors.toList());
            product.getVideos().addAll(productVideos);
        }

        // Save product entity to the database
        Seller seller = sellerRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("Seller not found with id: " + productDto.getSellerId()));
        product.setSeller(seller);
        Product savedProduct = productRepository.save(product);

        // Convert saved entity to DTO
        ProductDto savedProductDto = productMapper.toDto(savedProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDto);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));

        // Map the product entity to ProductDto
        ProductDto productDto = productMapper.toDto(product);

        return ResponseEntity.ok(productDto);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(productDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<String> deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product", "id", id.toString());
        }
        productRepository.deleteById(id);
        return ResponseEntity.ok("Product with ID " + id + " has been successfully deleted.");
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProductsByCategory(Long categoryId) {
        List<Product> products = productRepository.findByCategoryId(categoryId);
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProductsBySellerId(Long sellerId) {
        List<Product> products = productRepository.findBySellerId(sellerId);
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProductsWithoutCoupon() {
        List<Product> products = productRepository.findByCouponIsNull();
        List<ProductDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDtos);
    }


    @Override
    @Transactional
    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto, List<MultipartFile> images, List<MultipartFile> videos) {
        // Fetch the existing product
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "id", id.toString()));

        // Update product details using the mapper
        productMapper.updateProductFromDto(productDto, existingProduct);

        // Handle image uploads
        if (images != null && !images.isEmpty()) {
            List<Image> productImages = images.stream()
                    .map(image -> {
                        FileUploadUtil.assertAllowed(image, FileUploadUtil.IMAGE_PATTERN);
                        String fileName = FileUploadUtil.getFileName(
                                image.getOriginalFilename() != null ? image.getOriginalFilename().substring(0, image.getOriginalFilename().lastIndexOf('.')) : "image",
                                image.getOriginalFilename() != null ? image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.') + 1) : "jpg"
                        );
                        CloudinaryResponse response = cloudinaryService.uploadFile(image, fileName, "image");
                        Image productImage = new Image();
                        productImage.setImageUrl(response.getUrl());
                        productImage.setCloudinaryImageId(response.getPublicId());
                        productImage.setProduct(existingProduct);
                        return productImage;
                    })
                    .collect(Collectors.toList());
            existingProduct.getImages().addAll(productImages);
        }

        // Handle video uploads
        if (videos != null && !videos.isEmpty()) {
            List<Video> productVideos = videos.stream()
                    .map(video -> {
                        FileUploadUtil.assertAllowed(video, FileUploadUtil.VIDEO_PATTERN);
                        String fileName = FileUploadUtil.getFileName(
                                video.getOriginalFilename() != null ? video.getOriginalFilename().substring(0, video.getOriginalFilename().lastIndexOf('.')) : "video",
                                video.getOriginalFilename() != null ? video.getOriginalFilename().substring(video.getOriginalFilename().lastIndexOf('.') + 1) : "mp4"
                        );
                        CloudinaryResponse response = cloudinaryService.uploadFile(video, fileName, "video");
                        Video productVideo = new Video();
                        productVideo.setVideoUrl(response.getUrl());
                        productVideo.setCloudinaryVideoId(response.getPublicId());
                        productVideo.setProduct(existingProduct);
                        return productVideo;
                    })
                    .collect(Collectors.toList());
            existingProduct.getVideos().addAll(productVideos);
        }

        // Save the updated product entity to the database
        Product updatedProduct = productRepository.save(existingProduct);

        // Convert updated entity to DTO
        ProductDto updatedProductDto = productMapper.toDto(updatedProduct);

        return ResponseEntity.ok(updatedProductDto);
    }
}
