package com.marketplace.mapper;


import com.marketplace.dto.ProductViewDTO;
import com.marketplace.model.ProductView;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductViewMapper {
    @Mapping(target = "productId", source = "product.id")
    ProductViewDTO toDTO(ProductView productView);

    @Mapping(target = "product.id", source = "productId")
    ProductView toEntity(ProductViewDTO dto);
}
