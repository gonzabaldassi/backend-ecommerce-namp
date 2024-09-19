package com.namp.ecommerce.repository;
import com.namp.ecommerce.model.Subcategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISubcategoryDAO extends CrudRepository<Subcategory, Long> {
    List<Subcategory> findAll();
    Subcategory findByName(String name);
    Subcategory findByIdSubcategory(long id);
    Subcategory findById(long id);
}
