package com.marketplace.service.impl;

import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.dto.SellerDto;
import com.marketplace.enums.Role;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.SellerMapper;
import com.marketplace.model.Image;
import com.marketplace.model.Seller;
import com.marketplace.model.User;
import com.marketplace.repository.SellerRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.ISellerService;
import com.marketplace.util.FileUploadUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SellerServiceImpl implements ISellerService {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SellerMapper sellerMapper;

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public ResponseEntity<SellerDto> registerSeller(SellerDto dto, MultipartFile profileImage, MultipartFile shopCoverImage) {

        // Convert DTO to entity
        Seller seller = sellerMapper.toEntity(dto);
        seller.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            FileUploadUtil.assertAllowed(profileImage, FileUploadUtil.IMAGE_PATTERN);

            // Extract the base name and extension from the original file name
            String originalFileName = profileImage.getOriginalFilename();
            String baseName = originalFileName != null ? originalFileName.substring(0, originalFileName.lastIndexOf('.')) : "profile";
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "png";

            String fileName = FileUploadUtil.getFileName(baseName, extension);

            CloudinaryResponse response = cloudinaryService.uploadFile(profileImage, fileName, "image");
            Image profileImg = new Image();
            profileImg.setImageUrl(response.getUrl());
            profileImg.setCloudinaryImageId(response.getPublicId());
            profileImg.setUser(seller);
            seller.setProfileImage(profileImg);
        }

        // Handle shop cover image upload
        if (shopCoverImage != null && !shopCoverImage.isEmpty()) {
            FileUploadUtil.assertAllowed(shopCoverImage, FileUploadUtil.IMAGE_PATTERN);

            // Extract the base name and extension from the original file name
            String originalFileName = shopCoverImage.getOriginalFilename();
            String baseName = originalFileName != null ? originalFileName.substring(0, originalFileName.lastIndexOf('.')) : "cover";
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "png";

            String fileName = FileUploadUtil.getFileName(baseName, extension);

            CloudinaryResponse response = cloudinaryService.uploadFile(shopCoverImage, fileName, "image");
            Image coverImg = new Image();
            coverImg.setImageUrl(response.getUrl());
            coverImg.setCloudinaryImageId(response.getPublicId());
            coverImg.setUser(seller);
            seller.setShopCoverImage(coverImg);
        }

        // Save seller entity to the database
        seller.setRole(Role.SELLER);
        Seller savedSeller = sellerRepository.save(seller);

        // Convert saved entity to DTO
        SellerDto savedSellerDto = sellerMapper.toDTO(savedSeller);
        savedSellerDto.setProfileImage(savedSeller.getProfileImage() != null ? savedSeller.getProfileImage().getImageUrl() : null);
        savedSellerDto.setShopCoverImage(savedSeller.getShopCoverImage() != null ? savedSeller.getShopCoverImage().getImageUrl() : null);
        savedSellerDto.setCreatedAt(savedSeller.getCreatedAt());
        savedSellerDto.setUpdatedAt(savedSeller.getUpdatedAt());
        savedSellerDto.setSuspendedAt(savedSeller.getSuspendedAt());

        return ResponseEntity.ok(savedSellerDto);
    }



    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<SellerDto> getSellerById(Long id) {
        // Find the seller by id
        Seller seller = sellerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Seller","id",id.toString()));

        // Convert seller entity to DTO
        SellerDto sellerDto = sellerMapper.toDTO(seller);
        sellerDto.setProfileImage(seller.getProfileImage() != null ? seller.getProfileImage().getImageUrl() : null);
        sellerDto.setShopCoverImage(seller.getShopCoverImage() != null ? seller.getShopCoverImage().getImageUrl() : null);
        sellerDto.setCreatedAt(seller.getCreatedAt());
        sellerDto.setUpdatedAt(seller.getUpdatedAt());
        sellerDto.setSuspendedAt(seller.getSuspendedAt());

        return ResponseEntity.ok(sellerDto);
    }



}
