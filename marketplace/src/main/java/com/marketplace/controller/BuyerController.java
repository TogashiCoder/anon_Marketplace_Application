package com.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.BuyerRegistrationDTO;
import com.marketplace.model.Buyer;
import com.marketplace.service.impl.BuyerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/buyers")
public class BuyerController {

    @Autowired
    private BuyerServiceImpl buyerService;

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerBuyer(
            @RequestPart("buyer") String buyerJson,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BuyerRegistrationDTO buyerDTO = mapper.readValue(buyerJson, BuyerRegistrationDTO.class);

            Buyer registeredBuyer = buyerService.registerBuyer(buyerDTO, profileImage);
            return ResponseEntity.ok(registeredBuyer);
        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing buyer JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error registering buyer: " + e.getMessage());
        }
    }
}
