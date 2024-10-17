package com.namp.ecommerce.service;



import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.dto.SubcategoryWithProductsDTO;
import com.namp.ecommerce.model.Subcategory;

import java.util.List;


public interface ISubcategoryService {
    List<SubcategoryDTO> getSubcategories();
    List<SubcategoryWithProductsDTO> getSubcategoriesWithProducts();
    SubcategoryWithProductsDTO getSubcategoriesIdWithProducts(long id);
    SubcategoryDTO save(SubcategoryDTO subcategoryDTO);
    SubcategoryDTO update(SubcategoryDTO existingSubcategoryDTO, Subcategory subcategory);
    void delete(SubcategoryDTO subcategoryDTO);
    SubcategoryDTO findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long idSubcategory);

}
