package com.namp.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComboWithItDTO {

    private long idCombo;
    private String name;
    private String description;
    private String img;

    private double price;

    private List<ProductComboDTO> productCombo;
}
