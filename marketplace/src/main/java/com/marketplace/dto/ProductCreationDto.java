package com.marketplace.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ProductCreationDto {
    private String name;
    private String description;
    private BigDecimal basePrice;
    private Integer minimumOrderQuantity;
    private Long sellerId;
    private List<AttributeDto> attributes;
    private List<VariationDto> variations;

    @Data
    public static class AttributeDto {
        private String name;
        private List<String> options;
    }

    @Data
    public static class VariationDto {
        private String sku;
        private BigDecimal price;
        private Integer stockQuantity;
        private List<VariationAttributeDto> attributeValues;
    }

    @Data
    public static class VariationAttributeDto {
        private String attributeName;
        private String optionValue;
    }
}