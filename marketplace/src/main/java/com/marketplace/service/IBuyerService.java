package com.marketplace.service;

import com.marketplace.dto.BuyerDto;
import com.marketplace.model.Buyer;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IBuyerService {

    public ResponseEntity<BuyerDto> registerBuyer(BuyerDto dto, MultipartFile profileImage);

    }