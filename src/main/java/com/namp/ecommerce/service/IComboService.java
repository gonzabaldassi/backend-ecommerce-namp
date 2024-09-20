package com.namp.ecommerce.service;

import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithITProductComboDTO;
import com.namp.ecommerce.model.Combo;

import java.util.List;

public interface IComboService {
    List<ComboDTO> getCombos();
    List<ComboWithITProductComboDTO> getCombosWithITProductCombos();
    ComboDTO save(ComboDTO comboDTO);
    ComboDTO update(ComboDTO existingCombo, Combo combo);
    void delete(ComboDTO comboDTO);
    ComboDTO findById(long id);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long idProduct);
}
