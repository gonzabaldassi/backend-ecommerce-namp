package com.namp.ecommerce.dto;

import lombok.Data;

@Data
public class ProductComboDTO {
    private long idProductCombo;
    private long idProduct;
    private int cant;
}
