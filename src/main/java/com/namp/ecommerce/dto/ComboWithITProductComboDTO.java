package com.namp.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComboWithITProductComboDTO {

    private long idCombo;
    private String name;
    private String description;

    private double price;

    private List<ProductComboDTO> productCombo;
}
