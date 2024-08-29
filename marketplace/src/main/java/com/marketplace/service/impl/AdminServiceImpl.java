package com.marketplace.service.impl;

import com.marketplace.dto.AdminDto;
import com.marketplace.dto.BuyerDto;
import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.enums.Role;
import com.marketplace.mapper.AdminMapper;
import com.marketplace.model.Admin;
import com.marketplace.model.Buyer;
import com.marketplace.model.Image;
import com.marketplace.repository.AdminRepository;
import com.marketplace.service.IAdminService;
import com.marketplace.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl  implements IAdminService {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public ResponseEntity<AdminDto> registerAdmin(AdminDto dto, MultipartFile profileImage) {

        // Convert DTO to entity
        Admin admin = adminMapper.toEntity(dto);
        admin.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Handle profile image upload
        if (profileImage != null && !profileImage.isEmpty()) {
            FileUploadUtil.assertAllowed(profileImage, FileUploadUtil.IMAGE_PATTERN);
            String fileName = FileUploadUtil.getFileName(profileImage.getOriginalFilename());
            CloudinaryResponse response = cloudinaryService.uploadFile(profileImage, fileName, "image");
            Image image = new Image();
            image.setImageUrl(response.getUrl());
            image.setCloudinaryImageId(response.getPublicId());
            image.setUser(admin);
            admin.setProfileImage(image);
        }

        // Save buyer entity to the database
        admin.setRole(Role.ADMIN);
        Admin savedAdmin = adminRepository.save(admin);


        // Convert saved entity to DTO
        AdminDto savedAdminDto = adminMapper.toDTO(savedAdmin);
        savedAdminDto.setProfileImage(savedAdmin.getProfileImage().getImageUrl());
        savedAdminDto.setCreatedAt(savedAdmin.getCreatedAt());
        savedAdminDto.setUpdatedAt(null);
        savedAdminDto.setSuspendedAt(null);


        return ResponseEntity.ok(savedAdminDto);    }

}
