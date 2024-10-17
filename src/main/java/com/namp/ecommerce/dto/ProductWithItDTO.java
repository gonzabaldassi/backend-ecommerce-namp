package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductWithItDTO {
    private long idProduct;
    private String name;
    private String description;
    private double price;
    private int stock;
    private String img;

    private List<ProductComboDTO> productCombo;
}
