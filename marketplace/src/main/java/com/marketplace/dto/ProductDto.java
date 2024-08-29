package com.marketplace.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductDto {

    private String name;
    private String description;
    private BigDecimal basePrice;
    private BigDecimal discountPrice;
    private Long sellerId;
    private List<String> tags;
    private Integer minimumOrderQuantity;
}
