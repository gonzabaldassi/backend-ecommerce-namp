package com.namp.ecommerce.mapper;


import com.namp.ecommerce.dto.*;
import com.namp.ecommerce.model.*;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    //Metodo para convertir Category a CategoryDTO
    public CategoryDTO convertCategoryToDto(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();

        categoryDTO.setIdCategory(category.getIdCategory());
        categoryDTO.setName(category.getName());
        categoryDTO.setDescription(category.getDescription());

        return categoryDTO;
    }

   //Metodo para convertir Category a CategoryWithSubcategoriesDTO
    public CategoryWithSubcategoriesDTO convertCategoryWithSubcategoryToDto(Category category) {
        CategoryWithSubcategoriesDTO categoryWithSubcategoryDTO = new CategoryWithSubcategoriesDTO();

        categoryWithSubcategoryDTO.setIdCategory(category.getIdCategory());
        categoryWithSubcategoryDTO.setName(category.getName());
        categoryWithSubcategoryDTO.setDescription(category.getDescription());

        categoryWithSubcategoryDTO.setSubcategories(category.getSubcategories()
                .stream()
                .map(this::convertSubcategoryToDto)
                .collect(Collectors.toList()));

        return categoryWithSubcategoryDTO;
    }

    //Metodo para convertir Subcategory a SubcategoryDTO
    public SubcategoryDTO convertSubcategoryToDto(Subcategory subcategory){
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();

        subcategoryDTO.setIdSubcategory(subcategory.getIdSubcategory());
        subcategoryDTO.setName(subcategory.getName());
        subcategoryDTO.setDescription(subcategory.getDescription());

        subcategoryDTO.setCategoryName(subcategory.getIdCategory().getName());

        return subcategoryDTO;
    }

    //Metodo para convertir Subcategory a SubcategoryWithProductsDTO
    public SubcategoryWithProductsDTO convertSubcategoryWithProductsToDto(Subcategory subcategory) {
        SubcategoryWithProductsDTO subcategoryWithProductsDTO = new SubcategoryWithProductsDTO();

        subcategoryWithProductsDTO.setIdSubcategory(subcategory.getIdSubcategory());
        subcategoryWithProductsDTO.setName(subcategory.getName());
        subcategoryWithProductsDTO.setDescription(subcategory.getDescription());

        subcategoryWithProductsDTO.setCategoryName(subcategory.getIdCategory().getName());

        subcategoryWithProductsDTO.setProducts(subcategory.getProducts()
                .stream()
                .map(this::convertProductToDto)
                .collect(Collectors.toList()));

        return subcategoryWithProductsDTO;
    }

    //Metodo para convertir Product a ProductDTO
    public ProductDTO convertProductToDto(Product product) {
        ProductDTO productDTO = new ProductDTO();

        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setImg(product.getImg());
        productDTO.setSubcategoryName(product.getIdSubcategory().getName());

        return productDTO;
    }

    //Metodo para convertir Product a ProductWithITProductComboDTO
    public ProductWithITProductComboDTO convertProductWithITProductComboToDto(Product product) {
        ProductWithITProductComboDTO productDTO = new ProductWithITProductComboDTO();

        productDTO.setIdProduct(product.getIdProduct());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setImg(product.getImg());

        productDTO.setProductCombo(product.getProductCombo()
                .stream()
                .map(this::convertProductComboToDto)
                .collect(Collectors.toList()));

        return productDTO;
    }

    //Metodo para convertir Combo a ComboDTO
    public ComboDTO convertComboToDto(Combo combo) {
        ComboDTO comboDTO = new ComboDTO();

        comboDTO.setIdCombo(combo.getIdCombo());
        comboDTO.setName(combo.getName());
        comboDTO.setDescription(combo.getDescription());
        comboDTO.setPrice(combo.getPrice());

        return comboDTO;
    }

    //Metodo para convertir Combo a ComboWithITProductComboDTO
    public ComboWithITProductComboDTO convertComboWithITProductComboToDto(Combo combo) {
        ComboWithITProductComboDTO comboWithITProductComboDTO = new ComboWithITProductComboDTO();

        comboWithITProductComboDTO.setIdCombo(combo.getIdCombo());
        comboWithITProductComboDTO.setName(combo.getName());
        comboWithITProductComboDTO.setDescription(combo.getDescription());
        comboWithITProductComboDTO.setPrice(combo.getPrice());

        comboWithITProductComboDTO.setProductCombo(combo.getProductCombo()
                .stream()
                .map(this::convertProductComboToDto)
                .collect(Collectors.toList()));

        return comboWithITProductComboDTO;
    }

    //Metodo para convertir ProductCombo a ProductComboDTO
    public ProductComboDTO convertProductComboToDto(ProductCombo productCombo) {
        ProductComboDTO productComboDTO = new ProductComboDTO();

        productComboDTO.setIdProductCombo(productCombo.getIdProductCombo());
        productComboDTO.setCant(productCombo.getCant());

        return productComboDTO;
    }
}