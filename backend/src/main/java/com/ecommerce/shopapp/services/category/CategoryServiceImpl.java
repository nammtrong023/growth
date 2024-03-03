package com.ecommerce.shopapp.services.category;

import com.ecommerce.shopapp.dtos.CategoryDTO;
import com.ecommerce.shopapp.exceptions.DataNotFoundException;
import com.ecommerce.shopapp.models.Category;
import com.ecommerce.shopapp.models.Product;
import com.ecommerce.shopapp.repositories.CategoryRepository;
import com.ecommerce.shopapp.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;

    @Override
    @Transactional
    public Category createCategory(CategoryDTO categoryDTO) {
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();
        return categoryRepo.save(newCategory);
    }

    @Override
    public Category getCategoryById(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
    }

    @Override
    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Long categoryId, CategoryDTO categoryDTO) throws Exception {
        Category existingCategory = categoryRepo.findById(categoryId)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        existingCategory.setName(categoryDTO.getName());

        categoryRepo.save(existingCategory);
        return existingCategory;
    }

    @Override
    @Transactional
    public void deleteCategory(Long id) throws Exception {
        Category category = categoryRepo.findById(id)
                .orElseThrow(ChangeSetPersister.NotFoundException::new);

        List<Product> products = productRepo.findByCategory(category);

        if (!products.isEmpty()) {
            throw new Exception("Cannot delete category with associated products");
        } else {
            categoryRepo.deleteById(id);
        }
    }

    @Override
    public Category existingCategory(Long id) {
        return categoryRepo.findById(id)
                .orElseThrow(() -> new DataNotFoundException(
                        "Cannot find category with id: " + id));
    }
}
