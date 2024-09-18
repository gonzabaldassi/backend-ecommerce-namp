package com.namp.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class SubcategoryWithProductsDTO {

    private long idSubcategory;
    private String name;
    private String description;

    private String categoryName;

    private List<ProductDTO> products;
}
