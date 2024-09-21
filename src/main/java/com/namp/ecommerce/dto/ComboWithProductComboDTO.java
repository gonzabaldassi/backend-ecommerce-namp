package com.namp.ecommerce.dto;

import lombok.Data;
import java.util.List;

@Data
public class ComboWithProductComboDTO {

    private long idCombo;
    private String name;
    private String description;

    private double price;

    private List<ProductComboDTO> productCombo;
}

