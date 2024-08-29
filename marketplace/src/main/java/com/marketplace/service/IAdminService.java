package com.marketplace.service;

import com.marketplace.dto.AdminDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface IAdminService {


    public ResponseEntity<AdminDto> registerAdmin(AdminDto dto, MultipartFile profileImage);

}
