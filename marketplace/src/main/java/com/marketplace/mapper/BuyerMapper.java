package com.marketplace.mapper;


import com.marketplace.dto.BuyerDto;
import com.marketplace.model.Buyer;
import com.marketplace.model.Image;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BuyerMapper {


    @Mappings({
            @Mapping(source = "buyer.profileImage.imageUrl", target = "profileImage")
    })
    BuyerDto toDTO(Buyer buyer);

    @Mappings({
            @Mapping(target = "profileImage.imageUrl", source = "profileImage")
    })
    Buyer toEntity(BuyerDto buyerDto);

    List<BuyerDto> toDTOList(List<Buyer> buyers);

    List<Buyer> toEntityList(List<BuyerDto> buyerDtos);


}
