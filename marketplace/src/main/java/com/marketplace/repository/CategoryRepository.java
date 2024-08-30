package com.marketplace.repository;

import com.marketplace.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Find all categories that don't have a parent category (root categories).
     * @return List of root categories
     */
    List<Category> findByParentCategoryIsNull();

    /**
     * Find all subcategories of a given parent category.
     * @param parentCategory The parent category
     * @return List of subcategories
     */
    List<Category> findByParentCategory(Category parentCategory);

    /**
     * Find a category by its name.
     * @param name The name of the category
     * @return The category if found, or null
     */
    Category findByName(String name);

    /**
     * Find all active categories.
     * @return List of active categories
     */
    List<Category> findByIsActiveTrue();

    /**
     * Find all categories at a specific level in the hierarchy.
     * @param level The level to search for
     * @return List of categories at the specified level
     */
    List<Category> findByLevel(Integer level);
}
