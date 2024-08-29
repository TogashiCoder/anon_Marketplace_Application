package com.marketplace.service.impl;

import com.marketplace.dto.BuyerDto;
import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.enums.Role;
import com.marketplace.mapper.BuyerMapper;
import com.marketplace.model.Buyer;
import com.marketplace.model.Image;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.service.IBuyerService;
import com.marketplace.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BuyerServiceImpl implements IBuyerService {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private BuyerRepository buyerRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BuyerMapper buyerMapper;




    public ResponseEntity<BuyerDto> registerBuyer(BuyerDto dto, MultipartFile profileImage) {

        // Convert DTO to entity
        Buyer buyer = buyerMapper.toEntity(dto);
        buyer.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            FileUploadUtil.assertAllowed(profileImage, FileUploadUtil.IMAGE_PATTERN);

            // Extract the base name and extension from the original file name
            String originalFileName = profileImage.getOriginalFilename();
            String baseName = originalFileName != null ? originalFileName.substring(0, originalFileName.lastIndexOf('.')) : "profile";
            String extension = originalFileName != null ? originalFileName.substring(originalFileName.lastIndexOf('.') + 1) : "png";

            String fileName = FileUploadUtil.getFileName(baseName, extension);

            CloudinaryResponse response = cloudinaryService.uploadFile(profileImage, fileName, "image");
            Image image = new Image();
            image.setImageUrl(response.getUrl());
            image.setCloudinaryImageId(response.getPublicId());
            image.setUser(buyer);
            buyer.setProfileImage(image);
        }

        // Save buyer entity to the database
        buyer.setRole(Role.BUYER);
        Buyer savedBuyer = buyerRepository.save(buyer);

        // Convert saved entity to DTO
        BuyerDto savedBuyerDto = buyerMapper.toDTO(savedBuyer);
        savedBuyerDto.setProfileImage(savedBuyer.getProfileImage() != null ? savedBuyer.getProfileImage().getImageUrl() : null);
        savedBuyerDto.setCreatedAt(savedBuyer.getCreatedAt());
        savedBuyerDto.setUpdatedAt(savedBuyer.getUpdatedAt());
        savedBuyerDto.setSuspendedAt(savedBuyer.getSuspendedAt());

        return ResponseEntity.ok(savedBuyerDto);
    }



}
