package com.namp.ecommerce.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.namp.ecommerce.model.Combo;
import com.namp.ecommerce.model.ProductCombo;

public interface ProductComboDAO extends CrudRepository<ProductCombo,Long>{
    List<ProductCombo> findAll();
    Combo findByIdProdCombo(long id);
    Combo findById(long id);
}
