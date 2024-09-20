package com.namp.ecommerce.dto;

import lombok.Data;

@Data
public class ProductComboDTO {
    private long idProductCombo;
    private ProductDTO idProduct;
    private ComboDTO idCombo; 
    private int quantity;



}
