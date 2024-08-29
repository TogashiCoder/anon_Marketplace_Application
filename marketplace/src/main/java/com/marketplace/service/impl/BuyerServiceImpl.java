package com.marketplace.service.impl;

import com.marketplace.dto.BuyerRegistrationDTO;
import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.model.Buyer;
import com.marketplace.model.Image;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.service.IBuyerService;
import com.marketplace.util.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Transactional
    @Override
    public Buyer registerBuyer(BuyerRegistrationDTO dto, MultipartFile profileImage) {
        Buyer buyer = new Buyer();
        buyer.setFirstname(dto.getFirstname());
        buyer.setLastname(dto.getLastname());
        buyer.setGender(dto.getGender());
        buyer.setUsername(dto.getUsername());
        buyer.setEmail(dto.getEmail());
        buyer.setPhone(dto.getPhone());
        buyer.setPassword(passwordEncoder.encode(dto.getPassword()));
        buyer.setRole(dto.getRole());
        buyer.setStreet(dto.getStreet());
        buyer.setCity(dto.getCity());
        buyer.setState(dto.getState());
        buyer.setZipcode(dto.getZipcode());
        buyer.setCountry(dto.getCountry());
        buyer.setCreatedAt(LocalDateTime.now());

        if (profileImage != null && !profileImage.isEmpty()) {
            FileUploadUtil.assertAllowed(profileImage, FileUploadUtil.IMAGE_PATTERN);
            String fileName = FileUploadUtil.getFileName(profileImage.getOriginalFilename());
            CloudinaryResponse response = cloudinaryService.uploadFile(profileImage, fileName,"image");

            Image image = new Image();
            image.setImageUrl(response.getUrl());
            image.setCloudinaryImageId(response.getPublicId());
            //image.setBuyer(buyer); // Assuming the `Image` class has a `buyer` relationship

            buyer.setProfileImage(image);
        }

        return buyerRepository.save(buyer);
    }

}
