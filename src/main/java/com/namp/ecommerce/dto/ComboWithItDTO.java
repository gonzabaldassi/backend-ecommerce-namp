package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboWithItDTO {

    private long idCombo;
    private String name;
    private String description;
    private String img;

    private double price;

    private List<ProductComboDTO> productCombo;
}
