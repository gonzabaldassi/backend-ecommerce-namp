package com.namp.ecommerce.services;

import java.io.IOException;
import java.util.List;

import com.namp.ecommerce.models.Product;
import org.springframework.web.multipart.MultipartFile;

public interface IProductService {
    List<Product> getProducts();
    Product save(String product, MultipartFile file) throws IOException;
    Product update(Product existingProduct, Product product);
    void delete(Product product);
    Product findById(long id);
    boolean verifyName(String normalizedName);
}
