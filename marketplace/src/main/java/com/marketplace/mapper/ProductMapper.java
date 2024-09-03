package com.marketplace.mapper;

import com.marketplace.dto.ProductDto;
import com.marketplace.model.Image;
import com.marketplace.model.Product;
import com.marketplace.model.Video;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    ProductDto toDto(Product product);

    Product toEntity(ProductDto productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);

    void updateEntityFromDto(ProductDto productDto, @MappingTarget Product product);

}