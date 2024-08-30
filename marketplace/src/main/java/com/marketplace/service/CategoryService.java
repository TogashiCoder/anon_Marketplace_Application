package com.marketplace.service;

import com.marketplace.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(CategoryDto categoryDto);
    CategoryDto getCategoryById(Long id);
    List<CategoryDto> getAllCategories();
    CategoryDto updateCategory(Long id, CategoryDto categoryDto);
    void deleteCategory(Long id);
    List<CategoryDto> getRootCategories();
    List<CategoryDto> getSubcategories(Long id);
    CategoryDto activateCategory(Long id);
    CategoryDto deactivateCategory(Long id);

    }
