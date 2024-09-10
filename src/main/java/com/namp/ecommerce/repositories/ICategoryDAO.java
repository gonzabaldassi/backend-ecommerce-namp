package com.namp.ecommerce.repositories;

import com.namp.ecommerce.models.Category;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ICategoryDAO extends CrudRepository<Category, Long> {
    List<Category> findAll();
    Category findByName(String name);
}
