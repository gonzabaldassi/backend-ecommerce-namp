package com.namp.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductComboDTO {
    private long idProductCombo;
    private ProductDTO idProduct;
    private ComboDTO idCombo;
    private int quantity;
}
