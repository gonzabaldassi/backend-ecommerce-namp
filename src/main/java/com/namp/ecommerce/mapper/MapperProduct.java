package com.namp.ecommerce.mapper;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.ProductDTO;
import com.namp.ecommerce.dto.ProductWithItDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.repository.ISubcategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class MapperProduct {

    @Autowired
    private ISubcategoryDAO subcategoryDAO;

    @Autowired
    private MapperUtil mapperUtil;

    //Metodo para convertir de ProductDTO a Product
    public Product convertDtoToProduct(ProductDTO productDTO) {
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImg(productDTO.getImg());
        product.setIdSubcategory(subcategoryDAO.findByIdSubcategory(productDTO.getIdSubcategory().getIdSubcategory()));

        return product;
    }
        /*
    ----------------------------------------------------------------------------------------------------------
                                           MAPPER UTIL CALLS
   -----------------------------------------------------------------------------------------------------------
     */
    public ProductDTO convertProductToDto(Product product) {
        ProductDTO productDTO = mapperUtil.convertProductToDto(product);
        return productDTO;
    }

    public ProductWithItDTO convertProductWithItToDto(Product product) {
        ProductWithItDTO productDTO = mapperUtil.convertProductWithItToDto(product);
        return productDTO;
    }
}
