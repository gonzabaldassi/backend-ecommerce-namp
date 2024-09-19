package com.namp.ecommerce.dto;

import lombok.Data;

@Data
public class ProductDTO {
    private long idProduct;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String img;
    private SubcategoryDTO idSubcategory;
}
