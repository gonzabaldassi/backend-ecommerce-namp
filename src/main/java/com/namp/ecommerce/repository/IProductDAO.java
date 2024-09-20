package com.namp.ecommerce.repository;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.namp.ecommerce.model.Product;

public interface IProductDAO extends CrudRepository<Product, Long> {
    List<Product> findAll();
    Product findByName(String name);
    Product findByIdProduct(long id);
    Product findById(long id); 
}
