package com.namp.ecommerce.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import com.namp.ecommerce.models.Product;

public interface IProductDAO extends CrudRepository<Product, Long> {
    List<Product> findAll();
    Product findByName(String name);
    Product findByIdProduct(long id);
}
