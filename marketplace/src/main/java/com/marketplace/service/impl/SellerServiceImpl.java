package com.marketplace.service.impl;

import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.dto.SellerDto;
import com.marketplace.enums.Role;
import com.marketplace.mapper.SellerMapper;
import com.marketplace.model.Image;
import com.marketplace.model.Seller;
import com.marketplace.repository.SellerRepository;
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

    @Override
    @Transactional
    public ResponseEntity<SellerDto> registerSeller(SellerDto dto, MultipartFile profileImage, MultipartFile shopCoverImage) {
        // Convert DTO to entity
        Seller seller = sellerMapper.toEntity(dto);
        System.out.println(seller);
        seller.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            FileUploadUtil.assertAllowed(profileImage, FileUploadUtil.IMAGE_PATTERN);
            String fileName = FileUploadUtil.getFileName(profileImage.getOriginalFilename());
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
            String fileName = FileUploadUtil.getFileName(shopCoverImage.getOriginalFilename());
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
        savedSellerDto.setProfileImage(savedSeller.getProfileImage().getImageUrl());
        savedSellerDto.setShopCoverImage(savedSeller.getShopCoverImage().getImageUrl());
        savedSellerDto.setCreatedAt(savedSeller.getCreatedAt());
        savedSellerDto.setUpdatedAt(savedSeller.getUpdatedAt());
        savedSellerDto.setSuspendedAt(savedSeller.getSuspendedAt());

        return ResponseEntity.ok(savedSellerDto);
    }
}
