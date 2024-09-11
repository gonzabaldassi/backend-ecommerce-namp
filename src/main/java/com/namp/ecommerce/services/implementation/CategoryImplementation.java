package com.namp.ecommerce.services.implementation;

import com.namp.ecommerce.models.Category;
import com.namp.ecommerce.repositories.ICategoryDAO;
import com.namp.ecommerce.services.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryImplementation implements ICategoryService {

    @Autowired
    private ICategoryDAO categoryDAO;

    @Override
    public List<Category> getCategories() {
        return categoryDAO.findAll();
    }

    @Override
    public Category save(Category category) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = category.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        Category existingCategory = categoryDAO.findByName(normalizedName);

        if(existingCategory == null) {
            category.setName(normalizedName);

            return categoryDAO.save(category);
        }
        return null;
    }

    @Override
    public Category update(Category existingCategory,  Category category) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = category.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        Category repeatedCategory = categoryDAO.findByName(normalizedName);

        if(repeatedCategory != null) {
            return null;
        }

        existingCategory.setName(normalizedName);
        existingCategory.setDescription(category.getDescription());

        return categoryDAO.save(existingCategory);
    }

    @Override
    public void delete(Category category) {
        categoryDAO.delete(category);
    }

    @Override
    public Category findById(long id) {
        return categoryDAO.findByIdCategory(id);
    }
}
