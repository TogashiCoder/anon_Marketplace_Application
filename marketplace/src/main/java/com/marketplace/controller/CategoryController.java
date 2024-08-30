package com.marketplace.controller;


import com.marketplace.dto.CategoryDto;
import com.marketplace.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/admin/categories")
@Validated
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto createdCategory = categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> getCategoryById(@PathVariable Long id) {
        CategoryDto category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryDto categoryDto) {
        CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/root")
    public ResponseEntity<List<CategoryDto>> getRootCategories() {
        List<CategoryDto> rootCategories = categoryService.getRootCategories();
        return ResponseEntity.ok(rootCategories);
    }

    @GetMapping("/{id}/subcategories")
    public ResponseEntity<List<CategoryDto>> getSubcategories(@PathVariable Long id) {
        List<CategoryDto> subcategories = categoryService.getSubcategories(id);
        return ResponseEntity.ok(subcategories);
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryDto> activateCategory(@PathVariable Long id) {
        CategoryDto activatedCategory = categoryService.activateCategory(id);
        return ResponseEntity.ok(activatedCategory);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CategoryDto> deactivateCategory(@PathVariable Long id) {
        CategoryDto deactivatedCategory = categoryService.deactivateCategory(id);
        return ResponseEntity.ok(deactivatedCategory);
    }
}