package com.marketplace.service;

import com.marketplace.dto.SellerDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ISellerService {
    ResponseEntity<SellerDto> registerSeller(SellerDto dto, MultipartFile profileImage, MultipartFile coverImage);

}
