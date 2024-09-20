package com.namp.ecommerce.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithITProductComboDTO;
import com.namp.ecommerce.mapper.EntityDtoMapper;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.model.Combo;
import com.namp.ecommerce.repository.ICategoryDAO;
import com.namp.ecommerce.repository.IComboDAO;
import com.namp.ecommerce.service.IComboService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ComboImplementation implements IComboService{

    @Autowired
    private IComboDAO comboDAO;

    @Autowired
    private EntityDtoMapper entityDtoMapper;

    @Override
    public List<ComboDTO> getCombos() {
                return comboDAO.findAll()
                .stream()
                .map(entityDtoMapper::convertComboToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComboWithITProductComboDTO> getCombosWithITProductCombos() {
        return comboDAO.findAll()
        .stream()
        .map(entityDtoMapper::convertComboWithITProductComboToDto)
        .collect(Collectors.toList());
    }

    @Override
    public ComboDTO save(ComboDTO comboDTO) {
        String normalizedName = comboDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            comboDTO.setName(normalizedName);

            Combo combo = entityDtoMapper.convertDtoToCombo(comboDTO);

            Combo savedCombo = comboDAO.save(combo);

            return entityDtoMapper.convertComboToDto(savedCombo);
        }
        return null;    }

    @Override
    public ComboDTO update(ComboDTO existingComboDTO, Combo combo) {
        Combo existingCombo = comboDAO.findByIdCombo(existingComboDTO.getIdCombo());
        if (existingCombo == null) {
            return null;
        }

        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = combo.getName().replaceAll("\s+", " ").trim().toUpperCase();

        //Verifica que el nombre esta disponible
        if(verifyName(normalizedName,existingComboDTO.getIdCombo())) {
            return null; //Si el nombre ya esta siendo utilizado
        }

        //Actualizar los campos de la entidad existente
        existingCombo.setName(normalizedName);
        existingCombo.setDescription(combo.getDescription());

        //Guardar la categoria actualizada
        Combo updatedCombo = comboDAO.save(existingCombo);

        //Devolvemos el DTO de la categoria actualizada
        return entityDtoMapper.convertComboToDto(updatedCombo);
    }

    @Override
    public void delete(ComboDTO comboDTO) {
        Combo combo = comboDAO.findByIdCombo(comboDTO.getIdCombo());
        if (combo == null) {
            throw new EntityNotFoundException("Combo not found with ID: " + comboDTO.getIdCombo());
        }
        comboDAO.delete(combo);
    }

    @Override
    public boolean verifyName(String normalizedName) {
        List<Combo> combos = comboDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        for(Combo combo : combos){
            if(name.equals(combo.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean verifyName(String normalizedName, long idCombo) {
        List<Combo> combos = comboDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Verifica si se repite el nombre en las demas categorías, menos con la que se está actualizando
        for (Combo combo : combos) {
            if (combo.getIdCombo() != idCombo && name.equals(combo.getName().replaceAll("\s+", ""))) {
                return true;
            }
        }

        return false;
    }

    @Override
    public ComboDTO findById(long id) {
        Combo combo= comboDAO.findByIdCombo(id);
        if (combo!=null){
            return entityDtoMapper.convertComboToDto(combo);
        }
        return null;
    }
    
}
