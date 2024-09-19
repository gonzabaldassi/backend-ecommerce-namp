package com.namp.ecommerce.service;

import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithITProductComboDTO;
import com.namp.ecommerce.model.Combo;

import java.util.List;

public interface IComboService {
    List<ComboDTO> getCombos();
    List<ComboWithITProductComboDTO> getCombosWithITProductCombos();
    Combo save(Combo combo);
    ///Combo update(Combo combo);
    void delete(Combo combo);
    boolean verifyName(String normalizedName);
    boolean verifyName(String normalizedName, long idProduct);
}
