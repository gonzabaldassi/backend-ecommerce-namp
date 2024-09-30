package com.namp.ecommerce.controller;

import com.namp.ecommerce.dto.ProductComboDTO;
import com.namp.ecommerce.model.ProductCombo;
import com.namp.ecommerce.service.IProductComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import jakarta.validation.Valid;


@RestController
@RequestMapping("/api-namp")
public class ProductComboController {

    @Autowired
    private IProductComboService productComboService;

    @GetMapping("productCombo")
    public ResponseEntity<?> getProductCombos() {
        try {
            return ResponseEntity.ok(productComboService.getProductCombos());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error showing the Product Combos:" + e.getMessage());
        }
    }

    @PostMapping("productCombo")
    public ResponseEntity<?> addProductCombo(@Valid @RequestBody ProductComboDTO productComboDTO) {
        try {
            ProductComboDTO createdProductComboDTO = productComboService.save(productComboDTO);

            if (createdProductComboDTO == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("This Product Combo already exists");
            }

            return ResponseEntity.ok(createdProductComboDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the Product Combo:" + e.getMessage());
        }
    }

    @DeleteMapping("productCombo/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable long id) {
        try {
            ProductComboDTO productComboDTO = productComboService.findById(id);

            if (productComboDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The Product Combo does not exist");
            }

            productComboService.delete(productComboDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting the Product Combo: " + e.getMessage());
        }
    }

    @PutMapping("productCombo/{id}")
    public ResponseEntity<?> updateProductCombo(@PathVariable long id, @Valid @RequestBody ProductCombo productCombo) {
        try {
            ProductComboDTO existingProductComboDTO = productComboService.findById(id);

            if (existingProductComboDTO == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The Product Combo does not exist");
            }

            ProductComboDTO updatedProductComboDTO = productComboService.update(existingProductComboDTO, productCombo);

            if (updatedProductComboDTO == null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("This Product Combo already exists");
            }

            return ResponseEntity.ok(updatedProductComboDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating the Product Combo: " + e.getMessage());
        }


    }
}

