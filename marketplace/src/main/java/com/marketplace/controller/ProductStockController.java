package com.marketplace.controller;

import com.marketplace.dto.ProductStockDto;
import com.marketplace.service.impl.ProductStockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/product-stock")
public class ProductStockController {

    @Autowired
    private ProductStockService productStockService;

    @GetMapping("/seller/{sellerId}")
    public ResponseEntity<List<ProductStockDto>> getProductStockBySellerId(@PathVariable Long sellerId) {
        List<ProductStockDto> productStockList = productStockService.getProductStockBySellerId(sellerId);
        return ResponseEntity.ok(productStockList);
    }
}