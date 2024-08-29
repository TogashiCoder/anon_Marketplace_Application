package com.marketplace.mapper;

import com.marketplace.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SellerMapper {

    @Mapping(target = "id", source = "sellerId")
    Seller toEntity(Long sellerId);
}
