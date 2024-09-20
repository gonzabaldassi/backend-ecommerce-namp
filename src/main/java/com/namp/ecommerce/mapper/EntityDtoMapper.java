package com.namp.ecommerce.mapper;


import com.namp.ecommerce.dto.*;
import com.namp.ecommerce.model.*;
import com.namp.ecommerce.repository.ICategoryDAO;
import com.namp.ecommerce.repository.IComboDAO;
import com.namp.ecommerce.repository.IProductComboDAO;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.repository.ISubcategoryDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class EntityDtoMapper {

    @Autowired
    private ISubcategoryDAO subcategoryDAO;

    @Autowired
    private ICategoryDAO categoryDAO;

    @Autowired
    private IComboDAO comboDAO;

    @Autowired
    private IProductDAO productDAO;


    /*
    /---------------------------------------------------------------------------/
    /--------------------DTOs To Real Instances---------------------------------/
    /---------------------------------------------------------------------------/
    */


    //Metodo para convertir de CategoryDTO a Category
    public Category convertDtoToCategory(CategoryDTO categoryDTO) {
        Category category = new Category();

        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());

        return category;
    }

    //Metodo para convertir de SubcategoryDTO a Subcategory
    public Subcategory convertDtoToSubcategory(SubcategoryDTO subcategoryDTO) {
        Subcategory subcategory = new Subcategory();

        subcategory.setName(subcategoryDTO.getName());
        subcategory.setDescription(subcategoryDTO.getDescription());
        subcategory.setIdCategory(categoryDAO.findById(subcategoryDTO.getIdCategory().getIdCategory()));

        return subcategory;
    }

    //Metodo para convertir de ProductDTO a Product
    public Product convertDtoToProduct(ProductDTO productDTO) {
        Product product = new Product();

        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setImg(productDTO.getImg());
        product.setIdSubcategory(subcategoryDAO.findById(productDTO.getIdSubcategory().getIdSubcategory()));

        return product;
    }

    //Metodo para comvertir de ComboDTO a Combo 

    public Combo convertDtoToCombo(ComboDTO comboDTO){
        Combo combo = new Combo();
        
        combo.setName(comboDTO.getName());
        combo.setDescription(comboDTO.getDescription());
        combo.setPrice(comboDTO.getPrice());
        
        return combo; 
    }

    // Metodo para convertir de ProductComboDTO a ProductCombo
    public ProductCombo convertDtoToProductCombo(ProductComboDTO productComboDTO){
        ProductCombo productCombo = new ProductCombo();
        
        productCombo.setIdCombo(comboDAO.findById(productComboDTO.getIdCombo().getIdCombo()));
        productCombo.setIdProduct(productDAO.findById(productComboDTO.getIdProduct().getIdProduct()));
        productCombo.setQuantity(productComboDTO.getQuantity());
        
        return productCombo;
    }


    /*
    /---------------------------------------------------------------------------/
    /--------------------Real instances to DTOs---------------------------------/
    /---------------------------------------------------------------------------/
    */


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

        subcategoryDTO.setIdCategory(this.convertCategoryToDto(subcategory.getIdCategory()));

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
        //NO SIRVE MAS
        //productDTO.setSubcategoryName(product.getIdSubcategory().getName());
        productDTO.setIdSubcategory(this.convertSubcategoryToDto(product.getIdSubcategory()));

        return productDTO;
    }

    //Metodo para convertir Product a ProductWithITProductComboDTO
    public ProductWithProductComboDTO convertProductWithITProductComboToDto(Product product) {
        ProductWithProductComboDTO productDTO = new ProductWithProductComboDTO();

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
    public ComboWithProductComboDTO convertComboWithITProductComboToDto(Combo combo) {
        ComboWithProductComboDTO comboWithITProductComboDTO = new ComboWithProductComboDTO();

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
        productComboDTO.setQuantity(productCombo.getQuantity());

        return productComboDTO;
    }
}