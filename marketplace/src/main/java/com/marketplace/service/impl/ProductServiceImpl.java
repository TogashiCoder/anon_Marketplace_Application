package com.marketplace.service.impl;

import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.dto.ProductDto;
import com.marketplace.mapper.ProductMapper;
import com.marketplace.model.Image;
import com.marketplace.model.Product;
import com.marketplace.model.Seller;
import com.marketplace.model.Video;
import com.marketplace.repository.CategoryRepository;
import com.marketplace.repository.ProductRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.service.IProductService;
import com.marketplace.util.FileUploadUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CloudinaryService cloudinaryService;
    private final SellerRepository sellerRepository;
    private final CategoryRepository categoryRepository;

    @Override
    @Transactional
    public ResponseEntity<ProductDto> createProduct(ProductDto productDto, List<MultipartFile> images, List<MultipartFile> videos) {
        // Convert DTO to entity
        Product product = productMapper.toEntity(productDto);

        // Fetch the Category entity using categoryId
        var category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productDto.getCategoryId()));
        product.setCategory(category); // Set the category on the Product entity

        // Handle image uploads
        if (images != null && !images.isEmpty()) {
            for (MultipartFile image : images) {
                FileUploadUtil.assertAllowed(image, FileUploadUtil.IMAGE_PATTERN);
                // Extract the base name and extension from the original file name
                String originalFileName = image.getOriginalFilename();
                String baseName = originalFileName != null ? originalFileName.substring(0, originalFileName.lastIndexOf('.')) : "image";
                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "jpg";

                String fileName = FileUploadUtil.getFileName(baseName, extension);
                CloudinaryResponse response = cloudinaryService.uploadFile(image, fileName, "image");
                Image productImage = new Image();
                productImage.setImageUrl(response.getUrl());
                productImage.setCloudinaryImageId(response.getPublicId());
                productImage.setProduct(product);
                product.getImages().add(productImage);
            }
        }

        // Handle video uploads
        if (videos != null && !videos.isEmpty()) {
            for (MultipartFile video : videos) {
                FileUploadUtil.assertAllowed(video, FileUploadUtil.VIDEO_PATTERN);
                // Extract the base name and extension from the original file name
                String originalFileName = video.getOriginalFilename();
                String baseName = originalFileName != null ? originalFileName.substring(0, originalFileName.lastIndexOf('.')) : "video";
                String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "mp4";

                String fileName = FileUploadUtil.getFileName(baseName, extension);
                CloudinaryResponse response = cloudinaryService.uploadFile(video, fileName, "video");
                Video productVideo = new Video();
                productVideo.setVideoUrl(response.getUrl());
                productVideo.setCloudinaryVideoId(response.getPublicId());
                productVideo.setProduct(product);
                product.getVideos().add(productVideo);
            }
        }

        // Save product entity to the database
        Seller seller = sellerRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new IllegalArgumentException("Seller not found with id: " + productDto.getSellerId()));
        product.setSeller(seller);
        Product savedProduct = productRepository.save(product);

        // Convert saved entity to DTO
        ProductDto savedProductDto = productMapper.toDto(savedProduct);
        savedProductDto.setImageUrls(savedProduct.getImages().stream().map(Image::getImageUrl).toList());
        savedProductDto.setVideoUrls(savedProduct.getVideos().stream().map(Video::getVideoUrl).toList());
        savedProductDto.setSellerId(product.getSeller().getId());
        savedProductDto.setCategoryId(product.getCategory().getId()); // Set the category ID

        return ResponseEntity.status(HttpStatus.CREATED).body(savedProductDto);
    }

    @Override
    @Transactional
    public ResponseEntity<ProductDto> updateProduct(Long id, ProductDto productDto, List<MultipartFile> images, List<MultipartFile> videos) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Update the product properties
        productMapper.updateProductFromDto(productDto, product);

        // Fetch the Category entity using categoryId
        var category = categoryRepository.findById(productDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + productDto.getCategoryId()));
        product.setCategory(category); // Update the category on the Product entity

        // Handle image uploads (similar to createProduct)
        if (images != null && !images.isEmpty()) {
            // Handle image uploads as done in createProduct
        }

        // Handle video uploads (similar to createProduct)
        if (videos != null && !videos.isEmpty()) {
            // Handle video uploads as done in createProduct
        }

        Product updatedProduct = productRepository.save(product);

        ProductDto updatedProductDto = productMapper.toDto(updatedProduct);
        updatedProductDto.setImageUrls(updatedProduct.getImages().stream().map(Image::getImageUrl).toList());
        updatedProductDto.setVideoUrls(updatedProduct.getVideos().stream().map(Video::getVideoUrl).toList());
        updatedProductDto.setCategoryId(updatedProduct.getCategory().getId()); // Set the updated category ID

        return ResponseEntity.ok(updatedProductDto);
    }

    @Override
    public ResponseEntity<ProductDto> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        ProductDto productDto = productMapper.toDto(product);
        productDto.setImageUrls(product.getImages().stream().map(Image::getImageUrl).toList());
        productDto.setVideoUrls(product.getVideos().stream().map(Video::getVideoUrl).toList());
        productDto.setCategoryId(product.getCategory().getId()); // Set the category ID

        return ResponseEntity.ok(productDto);
    }

    @Override
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = products.stream()
                .map(product -> {
                    ProductDto dto = productMapper.toDto(product);
                    dto.setImageUrls(product.getImages().stream().map(Image::getImageUrl).toList());
                    dto.setVideoUrls(product.getVideos().stream().map(Video::getVideoUrl).toList());
                    dto.setCategoryId(product.getCategory().getId());
                    dto.setSellerId(product.getSeller().getId());
                    dto.setMinimumOrderQuantity(product.getMinimumOrderQuantity());
                    return dto;
                })
                .toList();

        return ResponseEntity.ok(productDtos);
    }

    @Override
    @Transactional
    public ResponseEntity<Void> deleteProduct(Long id) {
        productRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
