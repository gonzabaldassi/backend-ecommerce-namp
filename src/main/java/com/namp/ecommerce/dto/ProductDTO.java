package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {
    private long idProduct;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String img;
    private SubcategoryDTO idSubcategory;
}
