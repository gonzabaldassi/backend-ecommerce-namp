package com.namp.ecommerce.mapper;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapperCategory {

    @Autowired
    private MapperSubcategory mapperSubcategory;
    @Autowired
    private MapperUtil mapperUtil;

    //Metodo para mappear de CategoryDTO a Category

    public Category convertDtoToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return category;
    }
    /*
    ----------------------------------------------------------------------------------------------------------
                                           MAPPER UTIL CALLS
   -----------------------------------------------------------------------------------------------------------
     */
    public CategoryDTO convertCategoryToDto(Category category) {
        CategoryDTO categoryDTO = mapperUtil.convertCategoryToDto(category);
        return categoryDTO;
    }

    public CategoryWithSubcategoriesDTO convertCategoryWithSubcategoryToDto(Category category) {
        CategoryWithSubcategoriesDTO categoryWithSubcategoryDTO = mapperUtil.convertCategoryWithSubcategoryToDto(category);
        return categoryWithSubcategoryDTO;
    }

    public CategoryWithSubcategoriesDTO convertCategoryIdWithSubcategoryToDto(Category category) {
        CategoryWithSubcategoriesDTO categoryIdWithSubcategoryDTO = mapperUtil.convertCategoryIdWithSubcategoryToDto(category);
        return categoryIdWithSubcategoryDTO;
    }
}
