package com.namp.ecommerce.mapper;

import com.namp.ecommerce.dto.ProductComboDTO;
import com.namp.ecommerce.model.ProductCombo;
import com.namp.ecommerce.repository.IComboDAO;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.service.IProductComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MapperProductCombo {

    @Autowired
    private IProductDAO productDAO;

    @Autowired
    private IComboDAO comboDAO;

    @Autowired
    private MapperUtil mapperUtil;

    //Metodo para convertir de ProductComboDTO a ProductCombo
    public ProductCombo convertDtoToProductCombo(ProductComboDTO productComboDTO) {
        ProductCombo productCombo = new ProductCombo();

        productCombo.setQuantity(productComboDTO.getQuantity());
        productCombo.setIdProduct(productDAO.findByIdProduct(productComboDTO.getIdProduct().getIdProduct()));
        productCombo.setIdCombo(comboDAO.findByIdCombo(productComboDTO.getIdCombo().getIdCombo()));

        return productCombo;
    }
    /*
    ----------------------------------------------------------------------------------------------------------
                                           MAPPER UTIL CALLS
   -----------------------------------------------------------------------------------------------------------
     */
    //Metodo para convertir ProductCombo a ProductComboDTO
    public ProductComboDTO convertProductComboToDto(ProductCombo productCombo) {
        ProductComboDTO productComboDTO = mapperUtil.convertProductComboToDto(productCombo);
        return productComboDTO;
    }
}
