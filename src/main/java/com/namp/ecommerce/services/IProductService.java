package com.namp.ecommerce.services;

import java.util.List;

import com.namp.ecommerce.models.Product;

public interface IProductService {
    List<Product> getProducts();
    Product save(Product product);
    Product update(Product existingProduct, Product product);
    void delete(Product product);
    Product findById(long id);
    boolean verifyName(String normalizedName);
}
