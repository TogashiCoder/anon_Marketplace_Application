package com.marketplace.mapper;


import com.marketplace.dto.FollowerDTO;
import com.marketplace.model.Follower;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FollowerMapper {
    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "seller.id", target = "sellerId")
    FollowerDTO toDto(Follower follower);

    @Mapping(source = "buyerId", target = "buyer.id")
    @Mapping(source = "sellerId", target = "seller.id")
    Follower toEntity(FollowerDTO followerDTO);

}
