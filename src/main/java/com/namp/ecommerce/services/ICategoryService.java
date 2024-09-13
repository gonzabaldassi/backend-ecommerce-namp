package com.namp.ecommerce.services;

import com.namp.ecommerce.models.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getCategories();
    Category save(Category category);
    Category update(Category existingCategory, Category category);
    void delete(Category category);
    Category findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long categoryId);
}
