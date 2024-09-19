package com.namp.ecommerce.repository;

import com.namp.ecommerce.model.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICategoryDAO extends CrudRepository<Category, Long> {
    List<Category> findAll();
    Category findByName(String name);
    Category findByIdCategory(long id);
    Category findById(long id);
}
