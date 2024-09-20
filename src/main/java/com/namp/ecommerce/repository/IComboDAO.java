package com.namp.ecommerce.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import com.namp.ecommerce.model.Combo;

public interface IComboDAO extends CrudRepository<Combo, Long> {
    List<Combo> findAll();
    Combo findByName(String name);
    Combo findByIdCombo(long id);
    Combo findById(long id);
}
