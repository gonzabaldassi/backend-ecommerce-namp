package com.namp.ecommerce.service;



import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.dto.SubcategoryWithProductsDTO;
import com.namp.ecommerce.model.Subcategory;

import java.util.List;


public interface ISubcategoryService {
    List<SubcategoryDTO> getSubcategories();
    List<SubcategoryWithProductsDTO> getSubcategoriesWithProducts();
    Subcategory save(Subcategory subcategory);
    Subcategory update(Subcategory existingSubcategory, Subcategory subcategory);
    void delete(Subcategory subcategory);
    Subcategory findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long idSubcategory);

}
