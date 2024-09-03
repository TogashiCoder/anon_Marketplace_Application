package com.marketplace.service;

import com.marketplace.dto.CloudinaryResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ICloudinaryService {

    public CloudinaryResponse uploadFile(MultipartFile file, String fileName, String resourceType);
    public void deleteFile(String publicId, String resourceType);
}
