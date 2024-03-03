package com.ecommerce.shopapp.services.category;

import com.ecommerce.shopapp.dtos.CategoryDTO;
import com.ecommerce.shopapp.models.Category;

import java.util.List;

public interface CategoryService {
    Category createCategory(CategoryDTO category);
    Category getCategoryById(Long id);
    List<Category> getAllCategories();
    Category updateCategory(Long categoryId, CategoryDTO category) throws Exception;
    void deleteCategory(Long id) throws Exception;

    Category existingCategory(Long id);
}
