package com.namp.ecommerce.mapper;

import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithItDTO;
import com.namp.ecommerce.model.Combo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapperCombo {

    @Autowired
    private MapperUtil mapperUtil;

    //Metodo para convertir de ComboDTO a Combo
    public Combo convertDtoToCombo(ComboDTO comboDTO) {
        Combo combo = new Combo();

        combo.setName(comboDTO.getName());
        combo.setDescription(comboDTO.getDescription());
        combo.setPrice(comboDTO.getPrice());
        combo.setImg(comboDTO.getImg());

        return combo;
    }

    //Metodo para convertir de ComboWithIt a Combo
    public Combo convertDtoToComboWithIt(ComboWithItDTO comboWithItDTO) {
        Combo combo = new Combo();

        combo.setName(comboWithItDTO.getName());
        combo.setDescription(comboWithItDTO.getDescription());
        combo.setPrice(comboWithItDTO.getPrice());
        combo.setImg(comboWithItDTO.getImg());

        return combo;
    }
    /*
    ----------------------------------------------------------------------------------------------------------
                                           MAPPER UTIL CALLS
   -----------------------------------------------------------------------------------------------------------
     */
    public ComboDTO convertComboToDto(Combo combo) {
        ComboDTO comboDTO = mapperUtil.convertComboToDto(combo);
        return comboDTO;
    }

    public ComboWithItDTO convertComboWithItToDto(Combo combo) {
        ComboWithItDTO comboWithItDTO = mapperUtil.convertComboWithItToDto(combo);
        return comboWithItDTO;
    }
}
