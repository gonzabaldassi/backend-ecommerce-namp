package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoryWithProductsDTO {

    private long idSubcategory;
    private String name;
    private String description;

    private String categoryName;

    private List<ProductDTO> products;
}
