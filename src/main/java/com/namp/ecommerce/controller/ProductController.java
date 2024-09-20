package com.namp.ecommerce.controller;


import com.namp.ecommerce.dto.ProductDTO;
import com.namp.ecommerce.error.InvalidFileFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.service.IProductService;

import org.springframework.web.multipart.MultipartFile;



@RestController
@RequestMapping("/api-namp")
public class ProductController {

    @Autowired
    private IProductService productService;

    @GetMapping("product")
    public ResponseEntity<?> getProducts(){
        try{
            return ResponseEntity.ok(productService.getProducts());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al mostrar los productos:"+e.getMessage());
        }
    }

    @PostMapping("product")
    public ResponseEntity<?> createProduct(@RequestParam("product") String productJson, @RequestParam("file") MultipartFile file){
        try{
            ProductDTO createdProductDTO = productService.save(productJson, file);

            if (createdProductDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("This product already exists");
            }

            return ResponseEntity.ok(createdProductDTO);
        }catch (InvalidFileFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error creating the product");
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id){
        try{
            ProductDTO productDTO = productService.findById(id);

            if (productDTO == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The product does not exist");
            }

            productService.delete(productDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting the product: " + e.getMessage());
        }

    
    }

    @PutMapping("product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestParam("product") String productJson, @RequestParam(value = "file", required = false) MultipartFile file){
        try{
            ProductDTO existingProduct = productService.findById(id);

            if (existingProduct == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The product does not exist");
            }
            ProductDTO updatedProductDTO = productService.update(existingProduct, productJson, file);

            if (updatedProductDTO == null){
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("The entered name already exists");
            }

            return ResponseEntity.ok(updatedProductDTO);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error updating the product: " + e.getMessage());
        }
    }
    
}
