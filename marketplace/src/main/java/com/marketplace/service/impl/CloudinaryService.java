package com.marketplace.service.impl;

import com.cloudinary.Cloudinary;
import com.marketplace.dto.CloudinaryResponse;
import com.marketplace.exception.FuncErrorException;
import com.marketplace.service.ICloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class CloudinaryService implements ICloudinaryService {

    @Autowired
    private Cloudinary cloudinary;

    @Override
    public CloudinaryResponse uploadFile(MultipartFile file, String fileName, String resourceType) {
        try {
            final Map<String, Object> result = cloudinary.uploader().upload(file.getBytes(), Map.of(
                    "public_id", "nhndev/product/" + fileName,
                    "resource_type", resourceType
            ));
            final String url = (String) result.get("secure_url");
            final String publicId = (String) result.get("public_id");
            return CloudinaryResponse.builder().publicId(publicId).url(url).build();
        } catch (Exception e) {
            throw new FuncErrorException("Failed to upload file");
        }
    }

//
//    @Override
//    public void deleteFile(String publicId) {
//        try {
//            Map<String, Object> result = cloudinary.uploader().destroy(publicId, Map.of(
//                    "resource_type", "auto" // Automatically detects resource type (image, video, etc.)
//            ));
//            if (!"ok".equals(result.get("result"))) {
//                throw new FuncErrorException("Failed to delete file with public_id: " + publicId);
//            }
//        } catch (Exception e) {
//            throw new FuncErrorException("Error deleting file from Cloudinary: " + e.getMessage());
//        }
//    }


    @Override
    public void deleteFile(String publicId, String resourceType) {
        try {
            Map<String, String> params = new HashMap<>();
            params.put("resource_type", resourceType);

            cloudinary.uploader().destroy(publicId, params);
        } catch (IOException e) {
            throw new RuntimeException("Error deleting file from Cloudinary", e);
        }
    }



}
