package com.marketplace.mapper;


import com.marketplace.dto.CategoryDto;
import com.marketplace.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    @Mapping(target = "parentCategoryId", source = "parentCategory.id")
    @Mapping(target = "subCategoryIds", source = "subCategories", qualifiedByName = "subCategoriesToIds")
    CategoryDto toDto(Category category);

    @Mapping(target = "parentCategory", ignore = true)
    @Mapping(target = "subCategories", ignore = true)
    Category toEntity(CategoryDto categoryDto);

    @Named("subCategoriesToIds")
    default List<Long> subCategoriesToIds(List<Category> subCategories) {
        if (subCategories == null) {
            return null;
        }
        return subCategories.stream()
                .map(Category::getId)
                .collect(Collectors.toList());
    }
}
