package com.marketplace.service;

import com.marketplace.dto.CategoryDto;

import java.util.List;

public interface ICategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);
     CategoryDto setCategoryAsSubcategory(Long categoryId, Long parentCategoryId);
    public CategoryDto removeSubcategory(Long categoryId);
    CategoryDto getCategoryById(Long id);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
    List<CategoryDto> getRootCategories();
    List<CategoryDto> getSubcategories(Long id);
    List<String> getAllCategoriesNames();
    //CategoryDto activateCategory(Long id);
    //CategoryDto deactivateCategory(Long id);

    }
