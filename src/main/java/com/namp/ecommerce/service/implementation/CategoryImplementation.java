package com.namp.ecommerce.service.implementation;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.mapper.EntityDtoMapper;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.repository.ICategoryDAO;
import com.namp.ecommerce.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryImplementation implements ICategoryService {

    @Autowired
    private ICategoryDAO categoryDAO;

    @Autowired
    private EntityDtoMapper entityDtoMapper;

    @Override
    public List<CategoryDTO> getCategories(){
        return categoryDAO.findAll()
                .stream()
                .map(entityDtoMapper::convertCategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryWithSubcategoriesDTO> getCategoriesWithSubcategories(){
        return categoryDAO.findAll()
                .stream()
                .map(entityDtoMapper::convertCategoryWithSubcategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Category save(Category category) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = category.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            category.setName(normalizedName);

            return categoryDAO.save(category);
        }
        return null;
    }

    @Override
    public Category update(Category existingCategory,  Category category) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = category.getName().replaceAll("\s+", " ").trim().toUpperCase();

        if(verifyName(normalizedName,existingCategory.getIdCategory())) {
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

    @Override
    public boolean verifyName(String normalizedName){
        List<Category> categories = categoryDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Comparar el nombre de la categoria que se quiere guardar, con todos los demas sin espacio para ver si es el mismo
        for(Category category : categories){
            if(name.equals(category.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean verifyName(String normalizedName, long categoryId) {
        List<Category> categories = categoryDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Verifica si se repite el nombre en las demas categorías, menos con la que se está actualizando
        for (Category category : categories) {
            if (category.getIdCategory() != categoryId && name.equals(category.getName().replaceAll("\s+", ""))) {
                return true;
            }
        }

        return false;
    }
}
