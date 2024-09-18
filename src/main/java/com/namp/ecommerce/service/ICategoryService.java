package com.namp.ecommerce.service;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.model.Category;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getCategories();
    List<CategoryWithSubcategoriesDTO> getCategoriesWithSubcategories();
    Category save(Category category);
    Category update(Category existingCategory, Category category);
    void delete(Category category);
    Category findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long categoryId);
}
