package com.marketplace.service;

import com.marketplace.dto.BuyerRegistrationDTO;
import com.marketplace.model.Buyer;
import org.springframework.web.multipart.MultipartFile;

public interface IBuyerService {

    public Buyer registerBuyer(BuyerRegistrationDTO dto, MultipartFile profileImageFile);

    }
