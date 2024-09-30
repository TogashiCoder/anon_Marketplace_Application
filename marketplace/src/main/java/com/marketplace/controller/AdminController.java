package com.marketplace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.marketplace.dto.AdminDto;
import com.marketplace.dto.BuyerDto;
import com.marketplace.model.Admin;
import com.marketplace.service.IAdminService;
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
@RequestMapping("/api/admins")
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class AdminController {


    private final IAdminService adminService;

    private final AuthenticationService authenticationService;







    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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






//    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> registerAdmin(
//            @RequestPart("admin") String buyerJson,
//            @RequestPart(value = "profileImage", required = false) MultipartFile profileImage) {
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            AdminDto adminDto = mapper.readValue(buyerJson, AdminDto.class);
//
//            // Register the admin and get the response DTO
//            ResponseEntity<AdminDto> response = adminService.registerAdmin(adminDto, profileImage);
//            return response;
//
//        } catch (JsonProcessingException e) {
//            return ResponseEntity.badRequest().body("Error parsing admin JSON: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering admin: " + e.getMessage());
//        }
//    }


}
