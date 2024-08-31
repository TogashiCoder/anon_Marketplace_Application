package com.marketplace.service;

import com.marketplace.dto.ProductCreationDto;
import com.marketplace.model.Product;
import org.springframework.stereotype.Service;


public interface IProductService {

    Product createProduct(ProductCreationDto productDto);
}
