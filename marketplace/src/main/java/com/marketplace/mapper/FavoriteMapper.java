package com.marketplace.mapper;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.model.Favorite;
import org.mapstruct.*;


@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "product", target = "product")
    FavoriteDto toDto(Favorite favorite);

    @Mapping(source = "buyerId", target = "buyer.id")
    @Mapping(source = "product.id", target = "product.id")
    Favorite toEntity(FavoriteDto favoriteDto);
}