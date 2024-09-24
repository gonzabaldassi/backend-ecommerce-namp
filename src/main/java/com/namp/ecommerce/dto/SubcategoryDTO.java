package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryDTO {

    private long idSubcategory;
    private String name;
    private String description;

    private CategoryDTO idCategory;
}
