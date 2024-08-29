package com.marketplace.mapper;

import com.marketplace.dto.BuyerDto;
import com.marketplace.dto.ImageDto;
import com.marketplace.dto.SellerDto;
import com.marketplace.model.Image;
import com.marketplace.model.Seller;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SellerMapper {



    @Mappings({
            @Mapping(source = "seller.profileImage.imageUrl", target = "profileImage"),
            @Mapping(source = "seller.shopCoverImage.imageUrl", target = "shopCoverImage")
    })
    SellerDto toDTO(Seller seller);

    @Mappings({
            @Mapping(target = "profileImage.imageUrl", source = "profileImage"),
            @Mapping(target = "shopCoverImage.imageUrl", source = "shopCoverImage")
    })
    Seller toEntity(SellerDto sellerDto);

    List<SellerDto> toDTOList(List<Seller> sellers);

    List<Seller> toEntityList(List<SellerDto> sellerDtos);



}
