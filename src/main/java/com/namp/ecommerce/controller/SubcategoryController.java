package com.namp.ecommerce.controller;

import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.service.ISubcategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-namp")
public class SubcategoryController {

    @Autowired
    private ISubcategoryService subcategoryService;

    @GetMapping("subcategory")
    public ResponseEntity<?> getSubcategories(){
        try{
            return ResponseEntity.ok(subcategoryService.getSubcategoriesWithProducts());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al mostrar las categorias:"+e.getMessage());
        }
    }

    @PostMapping("subcategory")
    public ResponseEntity<?> createSubcategory(@Valid @RequestBody Subcategory subCategory) {
        try{
            Subcategory createdSubCategory = subcategoryService.save(subCategory);

            if (createdSubCategory == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Esta subCategoria ya se encuentra registrada");
            }

            return ResponseEntity.ok(createdSubCategory);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la subCategoria:"+e.getMessage());
        }
    }

    @DeleteMapping("subcategory/{id}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable long id){
        try{
            Subcategory subcategory = subcategoryService.findById(id);

            if (subcategory == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La subCategoria no existe");
            }

            subcategoryService.delete(subcategory);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la subCategoria: " + e.getMessage());
        }
    }

    @PutMapping("subcategory/{id}")
    public ResponseEntity<?> updateSubcategory(@PathVariable long id, @Valid @RequestBody Subcategory subcategory){
        try{
            Subcategory existinSubcategory = subcategoryService.findById(id);

            if (existinSubcategory == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La subcategoria no existe");
            }
            Subcategory updatedSubcategory = subcategoryService.update(existinSubcategory, subcategory);

            if (updatedSubcategory == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre ingresado ya existe");
            }

            return ResponseEntity.ok(updatedSubcategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la subcategoria: " + e.getMessage());
        }
    }
}

