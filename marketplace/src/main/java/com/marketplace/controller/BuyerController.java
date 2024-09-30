package com.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.BuyerDto;
import com.marketplace.service.IBuyerService;
import com.marketplace.service.securityService.AuthenticationService;
import com.marketplace.service.securityService.model.AuthenticationResponse;
import lombok.AllArgsConstructor;
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
@RequestMapping("/api/buyers")
@AllArgsConstructor(onConstructor_ = {@Autowired})
public class BuyerController {


    private final IBuyerService buyerService;
    private final AuthenticationService authenticationService;


    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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






//    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> registerBuyer(
//            @RequestPart("buyer") String buyerJson,
//            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            BuyerDto buyerDTO = mapper.readValue(buyerJson, BuyerDto.class);
//
//            // Register the buyer and get the response DTO
//            ResponseEntity<BuyerDto> response = buyerService.registerBuyer(buyerDTO, profileImage);
//            return response;
//
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body("Error parsing buyer JSON: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering buyer: " + e.getMessage());
//        }
//    }



}
