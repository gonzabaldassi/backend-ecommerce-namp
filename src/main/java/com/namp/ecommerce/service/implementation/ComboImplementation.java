package com.namp.ecommerce.service.implementation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithProductComboDTO;
import com.namp.ecommerce.dto.ProductComboDTO;
import com.namp.ecommerce.dto.ProductWithProductComboDTO;
import com.namp.ecommerce.mapper.EntityDtoMapper;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.model.Combo;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.model.ProductCombo;
import com.namp.ecommerce.repository.ICategoryDAO;
import com.namp.ecommerce.repository.IComboDAO;
import com.namp.ecommerce.repository.IProductComboDAO;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.service.IComboService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ComboImplementation implements IComboService{

    @Autowired
    private IComboDAO comboDAO;

    @Autowired
    private EntityDtoMapper entityDtoMapper;

    @Autowired
    private IProductDAO productDAO; 

    @Autowired
    private IProductComboDAO productComboDAO; 

    @Override
    public List<ComboDTO> getCombos() {
                return comboDAO.findAll()
                .stream()
                .map(entityDtoMapper::convertComboToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComboWithProductComboDTO> getCombosWithITProductCombos() {
        return comboDAO.findAll()
        .stream()
        .map(entityDtoMapper::convertComboWithITProductComboToDto)
        .collect(Collectors.toList());
    }

    @Override
    public ComboDTO save(ComboWithProductComboDTO comboWithProductComboDTO) {
    String normalizedName = comboWithProductComboDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();
    if (!verifyName(normalizedName)) {
        comboWithProductComboDTO.setName(normalizedName);

        // Convertir ComboWithProductComboDTO a entidad Combo
        Combo combo = entityDtoMapper.convertComboWithProductComboDTOToCombo(comboWithProductComboDTO);

        // Guardar el combo en la base de datos
        Combo savedCombo = comboDAO.save(combo);

        // Verificar si la lista de Porductos es Nula o Vacia 
        if (comboWithProductComboDTO.getProductCombo() == null || comboWithProductComboDTO.getProductCombo().isEmpty()) {
            throw new IllegalArgumentException("La lista de productos en el combo es null o está vacía.");
        }

        // Guardar los productos asociados en la tabla intermedia (ProductCombo)
        for (ProductComboDTO productComboDTO : comboWithProductComboDTO.getProductCombo()) {
            ProductCombo productCombo = new ProductCombo();

            // Obtener el producto desde el DAO
            Product product = productDAO.findById(productComboDTO.getIdProduct().getIdProduct());
            if (product == null) {
                throw new EntityNotFoundException("Producto no encontrado con el ID: " + productComboDTO.getIdProduct().getIdProduct());
            }

            // Asignar el combo y el producto en la entidad ProductCombo
            productCombo.setIdCombo(savedCombo);
            productCombo.setIdProduct(product);

            // Asignar la cantidad de producto en el combo
            productCombo.setQuantity(productComboDTO.getQuantity());

            // Guardar la relación en la tabla intermedia
            productComboDAO.save(productCombo);
        }

        // Retornar el ComboDTO del combo guardado
        return entityDtoMapper.convertComboToDto(savedCombo);
    }
        return null;    
    }

    // @Override
    // public ComboDTO save(ComboDTO comboDTO, List<ProductComboDTO> productComboDTOs) {
    //     // Convertir ComboDTO a entidad Combo
    //     Combo combo = entityDtoMapper.convertDtoToCombo(comboDTO);

    //     // Guardar el combo en la base de datos
    //     Combo savedCombo = comboDAO.save(combo);

    //     // Iterar sobre los productos del combo (ProductComboDTO)
    //     for (ProductComboDTO productComboDTO : productComboDTOs) {

    //         // Asociar el combo recién guardado al ProductComboDTO
    //         productComboDTO.setIdCombo(entityDtoMapper.convertComboToDto(savedCombo));

    //         // Buscar el producto por su ID (opcional, si no lo haces en el mapper)
    //         Product product = productDAO.findById(productComboDTO.getIdProduct().getIdProduct());

    //         // Asignar el producto al ProductComboDTO
    //         productComboDTO.setIdProduct(entityDtoMapper.convertProductToDto(product));

    //         // Convertir el DTO a la entidad ProductCombo
    //         ProductCombo productCombo = entityDtoMapper.convertDtoToProductCombo(productComboDTO);

    //         // Asignar la cantidad desde ProductComboDTO a ProductCombo
    //         productCombo.setQuantity(productComboDTO.getQuantity());

    //         // Guardar cada relación producto-combo en la tabla intermedia
    //         productComboDAO.save(productCombo);
    //     }

    //     // Retornar el ComboDTO del combo guardado
    //     return entityDtoMapper.convertComboToDto(savedCombo);
    // }
    

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
