package com.marketplace.service.impl;

import com.marketplace.model.*;
import com.marketplace.repository.*;
import com.marketplace.service.IProductService;
import com.marketplace.dto.ProductCreationDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class IProductServiceImpl implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SellerRepository sellerRepository;

    @Transactional
    @Override
    public Product createProduct(ProductCreationDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setBasePrice(productDto.getBasePrice());
        product.setMinimumOrderQuantity(productDto.getMinimumOrderQuantity());

        // Set the seller
        Seller seller = sellerRepository.findById(productDto.getSellerId())
                .orElseThrow(() -> new RuntimeException("Seller not found"));
        product.setSeller(seller);

        // Create and add attributes
        for (ProductCreationDto.AttributeDto attrDto : productDto.getAttributes()) {
            ProductAttribute attribute = new ProductAttribute();
            attribute.setName(attrDto.getName());
            attribute.setProduct(product);

            for (String optionValue : attrDto.getOptions()) {
                AttributeOption option = new AttributeOption();
                option.setValue(optionValue);
                option.setAttribute(attribute);
                attribute.getOptions().add(option);
            }

            product.getAttributes().add(attribute);
        }

        // Create and add variations
        for (ProductCreationDto.VariationDto varDto : productDto.getVariations()) {
            ProductVariation variation = new ProductVariation();
            variation.setSku(varDto.getSku());
            variation.setPrice(varDto.getPrice());
            variation.setStockQuantity(varDto.getStockQuantity());
            variation.setProduct(product);

            for (ProductCreationDto.VariationAttributeDto varAttrDto : varDto.getAttributeValues()) {
                ProductAttribute attribute = product.getAttributes().stream()
                        .filter(attr -> attr.getName().equals(varAttrDto.getAttributeName()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Attribute not found"));

                AttributeOption option = attribute.getOptions().stream()
                        .filter(opt -> opt.getValue().equals(varAttrDto.getOptionValue()))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Option not found"));

                VariationAttributeValue attrValue = new VariationAttributeValue();
                attrValue.setVariation(variation);
                attrValue.setAttribute(attribute);
                attrValue.setOption(option);
                variation.getAttributeValues().add(attrValue);
            }

            product.getVariations().add(variation);
        }

        return productRepository.save(product);
    }
}

