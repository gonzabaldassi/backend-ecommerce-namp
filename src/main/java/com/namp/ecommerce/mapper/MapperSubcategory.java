package com.namp.ecommerce.mapper;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.dto.SubcategoryWithProductsDTO;
import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.repository.ICategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapperSubcategory {

    @Autowired
    private ICategoryDAO categoryDAO;

    @Autowired
    private MapperUtil mapperUtil;

    //Metodo para convertir de SubcategoryDTO a Subcategory
    public Subcategory convertDtoToSubcategory(SubcategoryDTO subcategoryDTO) {
        Subcategory subcategory = new Subcategory();

        subcategory.setName(subcategoryDTO.getName());
        subcategory.setDescription(subcategoryDTO.getDescription());
        subcategory.setIdCategory(categoryDAO.findByIdCategory(subcategoryDTO.getIdCategory().getIdCategory()));

        return subcategory;
    }
    /*
    ----------------------------------------------------------------------------------------------------------
                                           MAPPER UTIL CALLS
   -----------------------------------------------------------------------------------------------------------
     */
    public SubcategoryDTO convertSubcategoryToDto(Subcategory subcategory){
        SubcategoryDTO subcategoryDTO = mapperUtil.convertSubcategoryToDto(subcategory);
        return subcategoryDTO;
    }

    public SubcategoryWithProductsDTO convertSubcategoryWithProductsToDto(Subcategory subcategory) {
        SubcategoryWithProductsDTO subcategoryWithProductsDTO = mapperUtil.convertSubcategoryWithProductsToDto(subcategory);
        return subcategoryWithProductsDTO;
    }

    public SubcategoryWithProductsDTO convertSubcategoryIdWithProductsToDto(Subcategory subcategory) {
        SubcategoryWithProductsDTO subcategoryIdWithProductsDTO = mapperUtil.convertSubcategoryIdWithProductsToDto(subcategory);
        return subcategoryIdWithProductsDTO;
    }
}
