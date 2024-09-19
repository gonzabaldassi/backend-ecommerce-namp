package com.namp.ecommerce.controller;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.service.ICategoryService;
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

    @GetMapping("category")
    public ResponseEntity<?> getCategories(){
        try{
            return ResponseEntity.ok(categoryService.getCategories());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the categories:"+e.getMessage());
        }
    }

    @GetMapping("categoryWithSubcategories")
    public ResponseEntity<?> getCategoriesWithSubcategories(){
        try{
            return ResponseEntity.ok(categoryService.getCategoriesWithSubcategories());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the categories:"+e.getMessage());
        }
    }

    @PostMapping("category")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDTO categoryDTO) {
        try{
            CategoryDTO createdCategoryDTO = categoryService.save(categoryDTO);

            if (createdCategoryDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("This category already exists");
            }

            return ResponseEntity.ok(createdCategoryDTO);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the category:"+e.getMessage());
        }
    }

    @DeleteMapping("category/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id){
        try{
            CategoryDTO categoryDTO = categoryService.findById(id);

            if (categoryDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The category does not exist");
            }

            categoryService.delete(categoryDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting the category: " + e.getMessage());
        }
    }

    @PutMapping("category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable long id, @Valid @RequestBody Category category){
        try{
            CategoryDTO existingCategoryDTO = categoryService.findById(id);

            if (existingCategoryDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The category does not exist");
            }

            CategoryDTO updatedCategoryDTO = categoryService.update(existingCategoryDTO,category);

            if (updatedCategoryDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("The entered name already exists");
            }

            return ResponseEntity.ok(updatedCategoryDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating the category: " + e.getMessage());
        }
    }
}
