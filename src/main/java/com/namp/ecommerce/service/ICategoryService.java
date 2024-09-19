package com.namp.ecommerce.service;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.model.Category;

import java.util.List;

public interface ICategoryService {
    List<CategoryDTO> getCategories();
    List<CategoryWithSubcategoriesDTO> getCategoriesWithSubcategories();
    CategoryDTO save(CategoryDTO categoryDTO);
    CategoryDTO update(CategoryDTO existingCategory, Category category);
    void delete(CategoryDTO categoryDTO);
    CategoryDTO findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long categoryId);
}
