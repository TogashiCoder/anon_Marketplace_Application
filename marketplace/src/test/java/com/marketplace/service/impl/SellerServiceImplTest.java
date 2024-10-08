package com.marketplace.service.impl;

import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.dto.SellerDto;
import com.marketplace.enums.Role;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.mapper.SellerMapper;
import com.marketplace.model.Seller;
import com.marketplace.model.Image;
import com.marketplace.repository.SellerRepository;
import com.marketplace.repository.UserRepository;
import com.marketplace.util.FileUploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SellerServiceImplTest {


    @Mock
    private SellerRepository sellerRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private SellerMapper sellerMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CloudinaryService cloudinaryService;

    @InjectMocks
    private SellerServiceImpl sellerService;

    private SellerDto sellerDto;
    private Seller seller;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        sellerDto = new SellerDto();
        sellerDto.setUsername("testSeller");
        sellerDto.setPassword("password");

        seller = new Seller();
        seller.setUsername("testSeller");
        seller.setRole(Role.SELLER);
    }

    @Test
    public void testRegisterSeller_withoutProfileImage() throws Exception {
        // Arrange
        MultipartFile profileImage = mock(MultipartFile.class);
        when(profileImage.isEmpty()).thenReturn(true);  // Simulate the case where no profile image is uploaded

        when(passwordEncoder.encode(sellerDto.getPassword())).thenReturn("encodedPassword");
        when(sellerMapper.toEntity(sellerDto)).thenReturn(seller);
        when(sellerRepository.save(seller)).thenReturn(seller);
        when(sellerMapper.toDTO(seller)).thenReturn(sellerDto);

        // Act
        ResponseEntity<SellerDto> response = sellerService.registerSeller(sellerDto, null, null);

        // Assert
        assertNotNull(response);
        assertNull(seller.getProfileImage());  // No profile image should be set
        verify(sellerRepository, times(1)).save(seller);

        // Additional assertions to ensure seller properties are set correctly
        assertEquals("testSeller", seller.getUsername());
        assertEquals(Role.SELLER, seller.getRole());
        assertEquals("encodedPassword", seller.getPassword());
    }


    @Test
    public void testGetSellerById_Success() {
        // Arrange
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));
        when(sellerMapper.toDTO(seller)).thenReturn(sellerDto);

        // Act
        ResponseEntity<SellerDto> response = sellerService.getSellerById(1L);

        // Assert
        assertNotNull(response);
        assertEquals("testSeller", response.getBody().getUsername());
        verify(sellerRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetSellerById_NotFound() {
        // Arrange
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.getSellerById(1L);
        });
    }

}