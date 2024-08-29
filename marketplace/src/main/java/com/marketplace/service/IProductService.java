package com.marketplace.service;

import com.marketplace.dto.ProductDto;
import com.marketplace.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface IProductService {

    public Product createProduct(ProductDto productDto);
}
