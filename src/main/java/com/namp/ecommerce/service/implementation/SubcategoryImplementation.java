package com.namp.ecommerce.service.implementation;

import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.dto.SubcategoryWithProductsDTO;
import com.namp.ecommerce.mapper.MapperSubcategory;
import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.repository.ISubcategoryDAO;
import com.namp.ecommerce.service.ISubcategoryService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubcategoryImplementation implements ISubcategoryService {

    @Autowired
    private ISubcategoryDAO subcategoryDAO;

    @Autowired
    private MapperSubcategory mapperSubcategory;

    @Override
    public List<SubcategoryDTO> getSubcategories(){
        return subcategoryDAO.findAll()
                .stream()
                .map(mapperSubcategory::convertSubcategoryToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubcategoryWithProductsDTO> getSubcategoriesWithProducts(){
        return subcategoryDAO.findAll()
                .stream()
                .map(mapperSubcategory::convertSubcategoryWithProductsToDto)
                .collect(Collectors.toList());
    }

    @Override
    public SubcategoryWithProductsDTO getSubcategoriesIdWithProducts(long id){
        Subcategory subcategory = subcategoryDAO.findByIdSubcategory(id);

        if (subcategory == null){
            return  null;
        }
        return mapperSubcategory.convertSubcategoryIdWithProductsToDto(subcategory);
    }


    @Override
    public SubcategoryDTO save(SubcategoryDTO subcategoryDTO) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = subcategoryDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            subcategoryDTO.setName(normalizedName);

            Subcategory subcategory = mapperSubcategory.convertDtoToSubcategory(subcategoryDTO);

            Subcategory savedSubcategory = subcategoryDAO.save(subcategory);

            return mapperSubcategory.convertSubcategoryToDto(savedSubcategory);
        }
        return null;

    }

    @Override
    public SubcategoryDTO update(SubcategoryDTO existingSubcategoryDTO, Subcategory subcategory) {

        //Buscar la categoria existente
        Subcategory existingSubcategory = subcategoryDAO.findByIdSubcategory(existingSubcategoryDTO.getIdSubcategory());
        if (existingSubcategory == null) {
            return null;
        }

        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = subcategory.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        //Verifica que el nombre esta disponible
        if(verifyName(normalizedName, existingSubcategoryDTO.getIdSubcategory())) {
            return null;
        }

        //Actualizar los campos de la entidad existente
        existingSubcategory.setName(normalizedName);
        existingSubcategory.setDescription(subcategory.getDescription());
        existingSubcategory.setIdCategory(subcategory.getIdCategory());

        //Guardar la subcategoria actualizada
        Subcategory updatedSubcategory = subcategoryDAO.save(existingSubcategory);

        //Devolvemos el DTO de la subcategoria actualizada
        return mapperSubcategory.convertSubcategoryToDto(updatedSubcategory);
    }

    @Override
    public void delete(SubcategoryDTO subcategoryDTO) {
        Subcategory subcategory = subcategoryDAO.findByIdSubcategory(subcategoryDTO.getIdSubcategory());
        if (subcategory == null) {
            throw new EntityNotFoundException("Subcategory not foundwith ID: " + subcategoryDTO.getIdSubcategory());
        }
        subcategoryDAO.delete(subcategory);
    }

    public SubcategoryDTO findById(long id) {
        Subcategory subcategory = subcategoryDAO.findByIdSubcategory(id);
        if (subcategory == null) {
            return null;
            }
        return mapperSubcategory.convertSubcategoryToDto(subcategory);

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
