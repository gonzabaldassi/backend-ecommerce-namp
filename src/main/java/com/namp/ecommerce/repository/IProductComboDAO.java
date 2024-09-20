package com.namp.ecommerce.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.namp.ecommerce.model.ProductCombo;

public interface IProductComboDAO extends CrudRepository<ProductCombo,Long>{
    List<ProductCombo> findAll();
    ProductCombo findByIdProductCombo(long id);
    ProductCombo findById(long id);
}

