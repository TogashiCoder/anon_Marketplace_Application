package com.marketplace.mapper;

import com.marketplace.dto.FavoriteDto;
import com.marketplace.model.Buyer;
import com.marketplace.model.Favorite;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;


//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface FavoriteMapper {
//
//    @Mapping(source = "buyer.id", target = "buyerId")
//    @Mapping(source = "product.id", target = "productId")
//    FavoriteDto toDto(Favorite favorite);
//
//    @Mapping(source = "buyerId", target = "buyer.id")
//    @Mapping(source = "productId", target = "product.id")
//    Favorite toEntity(FavoriteDto favoriteDto);
//}


//@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
//public interface FavoriteMapper {
//
//    @Mapping(source = "buyer.id", target = "buyerId")
//    @Mapping(source = "product", target = "product") // Updated
//    FavoriteDto toDto(Favorite favorite);
//
//    @Mapping(source = "buyerId", target = "buyer.id")
//    @Mapping(source = "product.id", target = "product.id")
//    Favorite toEntity(FavoriteDto favoriteDto);
//}

@Mapper(componentModel = "spring")
public interface FavoriteMapper {

    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "product", target = "product") // Updated to map the entire Product object
    FavoriteDto toDto(Favorite favorite);

    @Mapping(source = "buyerId", target = "buyer.id")
    @Mapping(source = "product.id", target = "product.id") // Map product ID to the Product entity
    Favorite toEntity(FavoriteDto favoriteDto);
}