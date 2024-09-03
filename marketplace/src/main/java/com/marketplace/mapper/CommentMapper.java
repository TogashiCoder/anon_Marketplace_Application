package com.marketplace.mapper;

import com.marketplace.dto.CommentDto;
import com.marketplace.exception.ResourceNotFoundException;
import com.marketplace.model.Buyer;
import com.marketplace.model.Comment;
import com.marketplace.model.Product;
import com.marketplace.repository.BuyerRepository;
import com.marketplace.repository.ProductRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {



    @Mapping(source = "buyer.id", target = "buyerId")
    @Mapping(source = "product.id", target = "productId")
    CommentDto toDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "buyer.id", source = "buyerId")
    @Mapping(target = "product.id", source = "productId")
    Comment toEntity(CommentDto commentDto);

    List<CommentDto> toDtoList(List<Comment> comments);
    List<Comment> toEntityList(List<CommentDto> commentDtos);
}
