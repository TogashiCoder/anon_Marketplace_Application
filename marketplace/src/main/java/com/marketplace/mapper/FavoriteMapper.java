package com.marketplace.mapper;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.model.Favorite;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FavoriteMapper {

    FavoriteMapper INSTANCE = Mappers.getMapper(FavoriteMapper.class);

    @Mapping(target = "buyerId", source = "buyer.id") // Map Buyer ID
    @Mapping(target = "productId", source = "product.id") // Map Product ID
    FavoriteDto toDto(Favorite favorite);

    @Mapping(target = "buyer.id", source = "buyerId") // Map Buyer from ID
    @Mapping(target = "product.id", source = "productId") // Map Product from ID
    Favorite toEntity(FavoriteDto favoriteDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFavoriteFromDto(FavoriteDto favoriteDto, @MappingTarget Favorite favorite);
}