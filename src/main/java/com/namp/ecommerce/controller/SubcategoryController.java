package com.namp.ecommerce.controller;

import com.namp.ecommerce.dto.SubcategoryDTO;
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
            return ResponseEntity.ok(subcategoryService.getSubcategories());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the subcategories:"+e.getMessage());
        }
    }

    @GetMapping("subcategoryWithProducts")
    public ResponseEntity<?> getSubcategoriesWithProducts(){
        try{
            return ResponseEntity.ok(subcategoryService.getSubcategoriesWithProducts());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the subcategories:"+e.getMessage());
        }
    }

    @GetMapping("subcategoryWithProducts/{id}")
    public ResponseEntity<?> getSubcategoriesIdWithProducts(@PathVariable long id){
        try{
            if (subcategoryService.getSubcategoriesIdWithProducts(id) == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Subcategory with ID "+id+" not found");
            }
            return ResponseEntity.ok(subcategoryService.getSubcategoriesWithProducts());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the subcategories:"+e.getMessage());
        }
    }

    @PostMapping("subcategory")
    public ResponseEntity<?> createSubcategory(@Valid @RequestBody SubcategoryDTO subcategoryDTO) {
        try{
            SubcategoryDTO createdSubCategoryDTO = subcategoryService.save(subcategoryDTO);

            if (createdSubCategoryDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("This subcategory already exists");
            }

            return ResponseEntity.ok(createdSubCategoryDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the subcategory:"+e.getMessage());
        }
    }

    @DeleteMapping("subcategory/{id}")
    public ResponseEntity<?> deleteSubcategory(@PathVariable long id){
        try{
            SubcategoryDTO subcategoryDTO = subcategoryService.findById(id);

            if (subcategoryDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The subcategory does not exist");
            }

            subcategoryService.delete(subcategoryDTO);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar la subCategoria: " + e.getMessage());
        }
    }

    @PutMapping("subcategory/{id}")
    public ResponseEntity<?> updateSubcategory(@PathVariable long id, @Valid @RequestBody Subcategory subcategory){
        try{
            SubcategoryDTO existinSubcategoryDTO = subcategoryService.findById(id);

            if (existinSubcategoryDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The subcategory does not exist");
            }
            SubcategoryDTO updatedSubcategoryDTO = subcategoryService.update(existinSubcategoryDTO, subcategory);

            if (updatedSubcategoryDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("The entered name already exists");
            }

            return ResponseEntity.ok(updatedSubcategoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating the subcategory: " + e.getMessage());
        }
    }
}

