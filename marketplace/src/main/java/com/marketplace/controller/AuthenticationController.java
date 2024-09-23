package com.marketplace.controller;


import com.marketplace.exception.InvalidLoginException;
import com.marketplace.service.securityService.AuthenticationService;
import com.marketplace.service.securityService.dto.UserLoginDto;
import com.marketplace.service.securityService.exception.CharacterNotFoundException;
import com.marketplace.service.securityService.model.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticationController {

    private final AuthenticationService authenticationService;



    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody UserLoginDto userLoginDto) {
        try {
            AuthenticationResponse response = authenticationService.authenticate(userLoginDto);
            return ResponseEntity.ok(response);
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new AuthenticationResponse(null, "Authentication failed: Please check your credentials and try again.", null));
        }
    }



}
