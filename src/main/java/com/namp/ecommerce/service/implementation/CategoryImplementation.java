package com.namp.ecommerce.service.implementation;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.mapper.EntityDtoMapper;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.repository.ICategoryDAO;
import com.namp.ecommerce.service.ICategoryService;
import jakarta.persistence.EntityNotFoundException;
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
    public CategoryWithSubcategoriesDTO getCategoriesIdWithSubcategories(long id){
        Category category = categoryDAO.findById(id);

        if (category == null){
            return null;
        }
        return entityDtoMapper.convertCategoryIdWithSubcategoryToDto(category);
    }

    @Override
    public CategoryDTO save(CategoryDTO categoryDTO) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = categoryDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            categoryDTO.setName(normalizedName);

            Category category = entityDtoMapper.convertDtoToCategory(categoryDTO);

            Category savedCategory = categoryDAO.save(category);

            return entityDtoMapper.convertCategoryToDto(savedCategory);
        }
        return null;
    }

    @Override
    public CategoryDTO update(CategoryDTO existingCategoryDTO,  Category category) {
        //Buscar la categoria existente
        Category existingCategory = categoryDAO.findByIdCategory(existingCategoryDTO.getIdCategory());
        if (existingCategory == null) {
            return null;
        }

        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = category.getName().replaceAll("\s+", " ").trim().toUpperCase();

        //Verifica que el nombre esta disponible
        if(verifyName(normalizedName,existingCategoryDTO.getIdCategory())) {
            return null; //Si el nombre ya esta siendo utilizado
        }

        //Actualizar los campos de la entidad existente
        existingCategory.setName(normalizedName);
        existingCategory.setDescription(category.getDescription());

        //Guardar la categoria actualizada
        Category updatedCategory = categoryDAO.save(existingCategory);

        //Devolvemos el DTO de la categoria actualizada
        return entityDtoMapper.convertCategoryToDto(updatedCategory);
    }

    @Override
    public void delete(CategoryDTO categoryDTO) {
        Category category = categoryDAO.findByIdCategory(categoryDTO.getIdCategory());
        if (category == null) {
            throw new EntityNotFoundException("Category not found with ID: " + categoryDTO.getIdCategory());
        }
        categoryDAO.delete(category);
    }

    @Override
    public CategoryDTO findById(long id) {
        Category category = categoryDAO.findByIdCategory(id);
        if (category != null) {
            return entityDtoMapper.convertCategoryToDto(category);
        }
        return null;
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
