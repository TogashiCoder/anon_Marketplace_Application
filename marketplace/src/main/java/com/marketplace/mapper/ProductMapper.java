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

    @Mapping(target = "imageUrls", expression = "java(mapImagesToUrls(product.getImages()))")
    @Mapping(target = "videoUrls", expression = "java(mapVideosToUrls(product.getVideos()))")
    @Mapping(target = "categoryId", source = "category.id") // Map Category ID
    @Mapping(target = "sellerId", source = "seller.id") // Map Seller ID
    ProductDto toDto(Product product);

    @Mapping(target = "images", ignore = true) // Avoid automatic mapping of images
    @Mapping(target = "videos", ignore = true) // Avoid automatic mapping of videos
    @Mapping(target = "category.id", source = "categoryId") // Map Category from ID
    @Mapping(target = "seller.id", source = "sellerId") // Map Seller from ID
    Product toEntity(ProductDto productDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateProductFromDto(ProductDto productDto, @MappingTarget Product product);

    default List<String> mapImagesToUrls(List<Image> images) {
        return images.stream().map(Image::getImageUrl).collect(Collectors.toList());
    }

    default List<String> mapVideosToUrls(List<Video> videos) {
        return videos.stream().map(Video::getVideoUrl).collect(Collectors.toList());
    }

    default List<Image> mapUrlsToImages(List<String> urls, Product product) {
        return urls.stream().map(url -> {
            Image image = new Image();
            image.setImageUrl(url);
            image.setProduct(product);
            return image;
        }).collect(Collectors.toList());
    }

    default List<Video> mapUrlsToVideos(List<String> urls, Product product) {
        return urls.stream().map(url -> {
            Video video = new Video();
            video.setVideoUrl(url);
            video.setProduct(product);
            return video;
        }).collect(Collectors.toList());
    }
}

