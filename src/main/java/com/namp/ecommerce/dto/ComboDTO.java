package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ComboDTO {

    private long idCombo;
    private String name;
    private String description;
    private String img;

    private double price;
}
