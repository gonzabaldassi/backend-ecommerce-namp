package com.namp.ecommerce.service;

import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithProductComboDTO;
import com.namp.ecommerce.dto.ProductComboDTO;
import com.namp.ecommerce.model.Combo;

import java.util.List;

public interface IComboService {
    List<ComboDTO> getCombos();
    List<ComboWithProductComboDTO> getCombosWithITProductCombos();
    ComboDTO save(ComboWithProductComboDTO comboWithProductComboDTO);
    ComboDTO update(ComboDTO existingCombo, Combo combo);
    void delete(ComboDTO comboDTO);
    ComboDTO findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long idProduct);
}
