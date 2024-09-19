package com.namp.ecommerce.dto;

import lombok.Data;

@Data
public class SubcategoryDTO {

    private long idSubcategory;
    private String name;
    private String description;

    private CategoryDTO idCategory;
}
