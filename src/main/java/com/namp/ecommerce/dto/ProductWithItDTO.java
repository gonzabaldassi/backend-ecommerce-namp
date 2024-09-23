package com.namp.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class ProductWithItDTO {
    private long idProduct;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String img;

    private List<ProductComboDTO> productCombo;
}
