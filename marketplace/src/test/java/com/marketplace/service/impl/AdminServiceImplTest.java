package com.marketplace.service.impl;

import com.marketplace.dto.AdminDto;
import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.enums.Role;
import com.marketplace.mapper.AdminMapper;
import com.marketplace.model.Admin;
import com.marketplace.model.Image;
import com.marketplace.repository.AdminRepository;
import com.marketplace.service.impl.AdminServiceImpl;
import com.marketplace.util.FileUploadUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private CloudinaryService cloudinaryService;

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AdminMapper adminMapper;

    @InjectMocks
    private AdminServiceImpl adminService;

    private AdminDto adminDto;
    private Admin admin;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminDto = new AdminDto();
        adminDto.setUsername("adminUser");
        adminDto.setPassword("password123");

        admin = new Admin();
        admin.setUsername("adminUser");
        admin.setPassword("encodedPassword");
    }

//    @Test
//    public void testRegisterAdmin_withProfileImage() throws Exception {
//        // Arrange
//        MultipartFile profileImage = mock(MultipartFile.class);
//        when(profileImage.isEmpty()).thenReturn(false);
//        when(profileImage.getOriginalFilename()).thenReturn("profile.png");
//
//        CloudinaryResponse cloudinaryResponse = new CloudinaryResponse("url", "publicId");
//        when(cloudinaryService.uploadFile(any(MultipartFile.class), anyString(), anyString())).thenReturn(cloudinaryResponse);
//
//        when(passwordEncoder.encode(adminDto.getPassword())).thenReturn("encodedPassword");
//        when(adminMapper.toEntity(adminDto)).thenReturn(admin);
//        when(adminRepository.save(admin)).thenReturn(admin);
//        when(adminMapper.toDTO(admin)).thenReturn(adminDto);
//
//        // Act
//        ResponseEntity<AdminDto> response = adminService.registerAdmin(adminDto, profileImage);
//
//        // Assert
//        assertNotNull(response);
//        assertEquals("url", admin.getProfileImage().getImageUrl());
//        verify(adminRepository, times(1)).save(admin);
//        verify(cloudinaryService, times(1)).uploadFile(profileImage, "profile", "image");
//    }

    @Test
    public void testRegisterAdmin_withoutProfileImage() throws Exception {
        // Arrange
        when(passwordEncoder.encode(adminDto.getPassword())).thenReturn("encodedPassword");
        when(adminMapper.toEntity(adminDto)).thenReturn(admin);
        when(adminRepository.save(admin)).thenReturn(admin);
        when(adminMapper.toDTO(admin)).thenReturn(adminDto);

        // Act
        ResponseEntity<AdminDto> response = adminService.registerAdmin(adminDto, null);

        // Assert
        assertNotNull(response);
        assertNull(admin.getProfileImage());
        assertEquals(Role.ADMIN, admin.getRole());
        verify(adminRepository, times(1)).save(admin);
        verify(cloudinaryService, never()).uploadFile(any(), anyString(), anyString());
    }

    @Test
    public void testRegisterAdmin_withEmptyProfileImage() throws Exception {
        // Arrange
        MultipartFile profileImage = mock(MultipartFile.class);
        when(profileImage.isEmpty()).thenReturn(true);

        when(passwordEncoder.encode(adminDto.getPassword())).thenReturn("encodedPassword");
        when(adminMapper.toEntity(adminDto)).thenReturn(admin);
        when(adminRepository.save(admin)).thenReturn(admin);
        when(adminMapper.toDTO(admin)).thenReturn(adminDto);

        // Act
        ResponseEntity<AdminDto> response = adminService.registerAdmin(adminDto, profileImage);

        // Assert
        assertNotNull(response);
        assertNull(admin.getProfileImage());
        assertEquals(Role.ADMIN, admin.getRole());
        verify(adminRepository, times(1)).save(admin);
        verify(cloudinaryService, never()).uploadFile(any(), anyString(), anyString());
    }




}