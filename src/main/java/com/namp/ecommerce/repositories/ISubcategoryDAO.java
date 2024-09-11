package com.namp.ecommerce.repositories;

import com.namp.ecommerce.models.Category;
import com.namp.ecommerce.models.Subcategory;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISubcategoryDAO extends CrudRepository<Subcategory, Long> {
    List<Subcategory> findAll();
    Subcategory findByName(String name);
    Subcategory findByIdSubcategory(long id);
}
