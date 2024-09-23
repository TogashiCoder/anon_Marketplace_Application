package com.marketplace.service.securityService;

import com.marketplace.dto.AdminDto;
import com.marketplace.dto.BuyerDto;
import com.marketplace.dto.SellerDto;
import com.marketplace.exception.InvalidLoginException;
import com.marketplace.mapper.AdminMapper;
import com.marketplace.mapper.BuyerMapper;
import com.marketplace.mapper.SellerMapper;
import com.marketplace.model.User;
import com.marketplace.repository.AdminRepository;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.SellerRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.service.IAdminService;
import com.marketplace.service.IBuyerService;
import com.marketplace.service.ISellerService;
import com.marketplace.service.securityService.dto.UserLoginDto;
import com.marketplace.service.securityService.exception.CharacterNotFoundException;
import com.marketplace.service.securityService.model.AuthenticationResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;
    private final BuyerRepository buyerRepository;
    private final AdminRepository adminRepository;
    private final IAdminService adminService;
    private final ISellerService sellerService;
    private final IBuyerService buyerService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final SellerMapper sellerMapper;
    private final BuyerMapper buyerMapper;
    private final AdminMapper adminMapper;



    public AuthenticationResponse registerAdmin(AdminDto adminDto, MultipartFile profileImage) {
        if(adminRepository.findByUsername(adminDto.getUsername()).isPresent()) {
            return new AuthenticationResponse(null,"Admin already exists",null);
        }

        ResponseEntity<AdminDto> response = adminService.registerAdmin(adminDto, profileImage);
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            AdminDto savedAdminDto = response.getBody();
            String jwt = jwtService.generateToken(adminMapper.toEntity(savedAdminDto));
            return new AuthenticationResponse(jwt, "Admin registration was successful", savedAdminDto.getId());
        }else{
            return new AuthenticationResponse(null,"Admin registration failed",null);
        }
    }


    public AuthenticationResponse registerBuyer(BuyerDto buyerDto, MultipartFile profileImage) {
        if(buyerRepository.findByUsername(buyerDto.getUsername()).isPresent()) {
            return new AuthenticationResponse(null,"Buyer already exists",null);
        }

        ResponseEntity<BuyerDto> response = buyerService.registerBuyer(buyerDto, profileImage);
        if(response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            BuyerDto savedBuyerDto = response.getBody();
            String jwt = jwtService.generateToken(buyerMapper.toEntity(savedBuyerDto));
            return new AuthenticationResponse(jwt, "Buyer registration was successful", savedBuyerDto.getId());
        }else{
            return new AuthenticationResponse(null,"Buyer registration failed",null);
        }
    }


    public AuthenticationResponse registerSeller(SellerDto sellerDto, MultipartFile profileImage, MultipartFile coverImage) {
        if (sellerRepository.findByUsername(sellerDto.getUsername()).isPresent()) {
            return new AuthenticationResponse(null, "Seller already exists", null);
        }

        ResponseEntity<SellerDto> response = sellerService.registerSeller(sellerDto, profileImage, coverImage);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            SellerDto savedSellerDto = response.getBody();
            String jwt = jwtService.generateToken(sellerMapper.toEntity(savedSellerDto));
            return new AuthenticationResponse(jwt, "Seller registration was successful", savedSellerDto.getId());
        } else {
            return new AuthenticationResponse(null, "Seller registration failed", null);
        }
    }





    public AuthenticationResponse authenticate(@Valid UserLoginDto request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new InvalidLoginException("Invalid credentials");
        } catch (AuthenticationException e) {
            throw new RuntimeException("Authentication failed", e);
        }

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new CharacterNotFoundException("No character found with username: " + request.getUsername()));

        String jwt = jwtService.generateToken(user);
        return new AuthenticationResponse(jwt, "Login was successful", user.getId());
    }




}
