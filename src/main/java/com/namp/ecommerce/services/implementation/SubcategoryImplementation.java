package com.namp.ecommerce.services.implementation;

import com.namp.ecommerce.models.Subcategory;
import com.namp.ecommerce.repositories.ISubcategoryDAO;
import com.namp.ecommerce.services.ISubcategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryImplementation implements ISubcategoryService {

    @Autowired
    private ISubcategoryDAO subcategoryDAO;

    @Override
    public List<Subcategory> getSubcategories() {
        return subcategoryDAO.findAll();
    }

    @Override
    public Subcategory save(Subcategory subcategory) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = subcategory.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            subcategory.setName(normalizedName);

            return subcategoryDAO.save(subcategory);
        }
        return null;

    }

    @Override
    public Subcategory update(Subcategory existingSubcategory, Subcategory subcategory) {
        String normalizedName = subcategory.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        // Verifica si el nombre esta repetido en la base de datos
        //Subcategory repeatedCategory = subcategoryDAO.findByName(normalizedName);

        // Esta en la base de datos == SI
        if(verifyName(normalizedName, existingSubcategory.getIdSubcategory())) {
            return null;
        }

        existingSubcategory.setName(normalizedName);
        existingSubcategory.setDescription(subcategory.getDescription());
        existingSubcategory.setIdCategory(subcategory.getIdCategory());

        return subcategoryDAO.save(existingSubcategory);
    }

    @Override
    public void delete(Subcategory subcategory) {
        subcategoryDAO.delete(subcategory);

    }

    public Subcategory findById(long id) {
        return subcategoryDAO.findByIdSubcategory(id);
    }

    @Override
    public boolean verifyName(String normalizedName){
        List<Subcategory> subcategories = subcategoryDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Comparar el nombre de la categoria que se quiere guardar, con todos los demas sin espacio para ver si es el mismo
        for(Subcategory subcategory : subcategories){
            if(name.equals(subcategory.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean verifyName(String normalizedName, long idSubcategory){
        List<Subcategory> subcategories = subcategoryDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Verifica si se repite el nombre en las demas subcategorías, menos con la que se está actualizando
        for(Subcategory subcategory : subcategories){
            if(subcategory.getIdSubcategory() != idSubcategory && name.equals(subcategory.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }


}
