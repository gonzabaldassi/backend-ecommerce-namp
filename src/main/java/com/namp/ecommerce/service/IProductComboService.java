package com.namp.ecommerce.service;

import java.util.List;

import com.namp.ecommerce.dto.ProductComboDTO;

import com.namp.ecommerce.model.Combo;
import com.namp.ecommerce.model.ProductCombo;

public interface IProductComboService {
    List<ProductComboDTO> getProductCombos();
    ProductComboDTO save(ProductComboDTO productComboDTO);
    ProductComboDTO update(ProductComboDTO existingProductComboDTO, ProductCombo productCombo);
    void delete(ProductComboDTO productComboDTO);
    ProductComboDTO findById(long id);
}
