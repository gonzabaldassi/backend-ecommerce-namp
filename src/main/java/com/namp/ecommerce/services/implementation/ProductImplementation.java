package com.namp.ecommerce.services.implementation;
import com.namp.ecommerce.models.Product;
import com.namp.ecommerce.repositories.IProductDAO;
import com.namp.ecommerce.services.IProductService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductImplementation implements IProductService{

    @Autowired
    private IProductDAO productDAO;

    @Override
    public List<Product> getProducts() {
        return productDAO.findAll();
    }

    @Override
    public Product save(Product product) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = product.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            product.setName(normalizedName);
            return productDAO.save(product);
        }
        return null;
    }

    @Override
    public Product update(Product existingProduct, Product product) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = product.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(verifyName(normalizedName)) {
            return null;
        }

        existingProduct.setName(normalizedName);
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setIdSubcategory(product.getIdSubcategory());

        return productDAO.save(existingProduct);
    }

    @Override
    public void delete(Product product) {
        productDAO.delete(product);
    }

    @Override
    public Product findById(long id) {
        return productDAO.findByIdProduct(id);
    }

    @Override
    public boolean verifyName(String normalizedName) {
        List<Product> products = productDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Comparar el nombre de la categoria que se quiere guardar, con todos los demas sin espacio para ver si es el mismo
        for(Product product : products){
            if(name.equals(product.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }
    
}
