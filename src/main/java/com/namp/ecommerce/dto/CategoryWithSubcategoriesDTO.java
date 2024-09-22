package com.namp.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CategoryWithSubcategoriesDTO {

    private long idCategory;
    private String name;
    private String description;

    private List<SubcategoryDTO> subcategories;
    private List<SubcategoryWithProductsDTO> subcategoryWithProducts;
}
