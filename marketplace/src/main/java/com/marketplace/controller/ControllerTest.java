package com.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.AdminDto;
import com.marketplace.dto.BuyerDto;
import com.marketplace.dto.SellerDto;
import com.marketplace.service.securityService.AuthenticationService;
import com.marketplace.service.securityService.model.AuthenticationResponse;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/test/")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class ControllerTest {



    private final AuthenticationService authenticationService;

    @PostMapping(value = "/seller/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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














    @PostMapping(value = "/buyer/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerBuyer(
            @RequestPart("buyer") String buyerJson,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            BuyerDto buyerDTO = mapper.readValue(buyerJson, BuyerDto.class);

            // Use AuthenticationService to handle buyer registration and JWT generation
            AuthenticationResponse response = authenticationService.registerBuyer(buyerDTO, profileImage);

            if (response.getAccessToken() != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing buyer JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering buyer: " + e.getMessage());
        }
    }





    @PostMapping(value = "/admin/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> registerAdmin(
            @RequestPart("admin") String adminJson,
            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            AdminDto adminDto = mapper.readValue(adminJson, AdminDto.class);

            AuthenticationResponse response = authenticationService.registerAdmin(adminDto, profileImage);

            if (response.getAccessToken() != null) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }

        } catch (JsonProcessingException e) {
            return ResponseEntity.badRequest().body("Error parsing admin JSON: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error registering admin: " + e.getMessage());
        }
    }






}