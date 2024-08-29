package com.marketplace.mapper;

import com.marketplace.dto.AdminDto;
import com.marketplace.dto.BuyerDto;
import com.marketplace.model.Admin;
import com.marketplace.model.Buyer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


@Mapper(componentModel = "spring")
public interface AdminMapper {

    @Mappings({
            @Mapping(source = "admin.profileImage.imageUrl", target = "profileImage")
    })
    AdminDto toDTO(Admin admin);

    @Mappings({
            @Mapping(target = "profileImage.imageUrl", source = "profileImage")
    })
    Admin toEntity(AdminDto adminDto);

    List<AdminDto> toDTOList(List<Admin> admins);

    List<Admin> toEntityList(List<AdminDto> adminDtos);


}
