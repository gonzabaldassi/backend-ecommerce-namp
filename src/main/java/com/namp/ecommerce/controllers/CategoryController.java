package com.namp.ecommerce.controllers;

import com.namp.ecommerce.models.Category;
import com.namp.ecommerce.services.ICategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api-namp")
public class CategoryController {
    @Autowired
    private ICategoryService categoryService;

    @PostMapping("category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {
        try{
            Category createdCategory = categoryService.save(category);

            if (createdCategory == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("Esta categoria ya se encuentra registrada");
            }

            return ResponseEntity.ok(createdCategory);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la categoria:"+e.getMessage());
        }
    }

    @GetMapping("category")
    public ResponseEntity<?> getCategories(){
        try{
            return ResponseEntity.ok(categoryService.getCategories());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al mostrar las categorias:"+e.getMessage());
        }
    }

    @DeleteMapping("category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id){
        try{
            Category category = categoryService.findById(id);

            if (category == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe");
            }

            categoryService.delete(category);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la categoria: " + e.getMessage());
        }
    }


    @PutMapping("category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @Valid @RequestBody Category category){
        try{
            Category existingCategory = categoryService.findById(id);

            if (existingCategory == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoria no existe");
            }

            Category updatedCategory = categoryService.update(existingCategory, category);

            if (updatedCategory == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre ingresado ya existe");
            }

            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar la categoria: " + e.getMessage());
        }
    }
}
