package com.marketplace.controller;

import com.marketplace.dto.SellerDto;
import com.marketplace.service.ISellerService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.service.securityService.AuthenticationService;
import com.marketplace.service.securityService.model.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/sellers")
public class SellerController {

    @Autowired
    private ISellerService sellerService;
    @Autowired
    private AuthenticationService authenticationService;



    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerSeller(
            @RequestPart("seller") String sellerJson,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            SellerDto sellerDTO = mapper.readValue(sellerJson, SellerDto.class);

            AuthenticationResponse response = authenticationService.registerSeller(sellerDTO, profileImage, coverImage);

            if (response.getAccessToken() != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing seller JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering seller: " + e.getMessage());
        }
    }


















    //work very good (the register before jwt)
//    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> registerSeller(
//            @RequestPart("seller") String sellerJson,
//            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage,
//            @RequestPart(value = "coverImage", required = false) MultipartFile coverImage) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            SellerDto sellerDTO = mapper.readValue(sellerJson, SellerDto.class);
//
//            // Register the seller and get the response DTO
//            ResponseEntity<SellerDto> response = sellerService.registerSeller(sellerDTO, profileImage, coverImage);
//            return response;
//
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body("Error parsing seller JSON: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering seller: " + e.getMessage());
//        }
//    }

}
