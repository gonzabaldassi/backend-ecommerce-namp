package com.namp.ecommerce.service.implementation;

import java.util.List;
import java.util.stream.Collectors;

import com.namp.ecommerce.mapper.MapperProductCombo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.namp.ecommerce.dto.ProductComboDTO;
import com.namp.ecommerce.model.ProductCombo;
import com.namp.ecommerce.repository.IProductComboDAO;
import com.namp.ecommerce.service.IProductComboService;

import jakarta.persistence.EntityNotFoundException;



@Service
public class ProductComboImplementation implements IProductComboService {

    @Autowired
    private IProductComboDAO productComboDAO;

    @Autowired
    private MapperProductCombo mapperProductCombo;

    @Override
    public List<ProductComboDTO> getProductCombos() {
        return productComboDAO.findAll()
                .stream()
                .map(mapperProductCombo::convertProductComboToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductComboDTO save(ProductComboDTO productComboDTO) {

        ProductCombo productCombo = mapperProductCombo.convertDtoToProductCombo(productComboDTO);

        ProductCombo savedProductCombo = productComboDAO.save(productCombo);

        return mapperProductCombo.convertProductComboToDto(savedProductCombo);
    }

    @Override
    public ProductComboDTO update(ProductComboDTO existingProductComboDTO, ProductCombo productCombo) {

        //Buscar la categoria existente
        ProductCombo existingProductCombo = productComboDAO.findByIdProductCombo(existingProductComboDTO.getIdProductCombo());
        if (existingProductCombo == null) {
            return null;
        }

        //Actualizar los campos de la entidad existente
        existingProductCombo.setIdCombo(productCombo.getIdCombo());
        existingProductCombo.setIdProduct(productCombo.getIdProduct());
        existingProductCombo.setQuantity(productCombo.getQuantity());

        //Guardar la categoria actualizada
        ProductCombo updateProductCombo = productComboDAO.save(existingProductCombo);

        //Devolvemos el DTO de la categoria actualizada
        return mapperProductCombo.convertProductComboToDto(updateProductCombo);
    }

    @Override
    public void delete(ProductComboDTO productComboDTO) {
        ProductCombo productCombo = productComboDAO.findByIdProductCombo(productComboDTO.getIdProductCombo());
        if (productCombo == null) {
            throw new EntityNotFoundException("ProductComnbo not foundwith ID: " + productComboDTO.getIdProductCombo());
        }
        productComboDAO.delete(productCombo);
    }

    @Override
    public ProductComboDTO findById(long id) {
        ProductCombo productCombo = productComboDAO.findByIdProductCombo(id);
        if (productCombo == null) {
            return null;
        }
        return mapperProductCombo.convertProductComboToDto(productCombo);
    }
}