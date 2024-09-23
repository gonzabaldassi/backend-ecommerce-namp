package com.namp.ecommerce.dto;

import lombok.Data;

@Data
public class ComboDTO {

    private long idCombo;
    private String name;
    private String description;
    private String img;

    private double price;
}
