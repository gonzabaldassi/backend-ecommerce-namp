package com.namp.ecommerce.controller;


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
            Product createdProduct = productService.save(productJson, file);

            if (createdProduct == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Este producto ya se encuentra registrado");
            }

            return ResponseEntity.ok(createdProduct);
        }catch (InvalidFileFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear el producto");
        }
    }

    @DeleteMapping("product/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable long id){
        try{
            Product product= productService.findById(id);

            if (product == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La subCategoria no existe");
            }

            productService.delete(product);


            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto: " + e.getMessage());
        }

    
    }

    @PutMapping("product/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable long id, @RequestParam("product") String productJson, @RequestParam("file") MultipartFile file){
        try{
            Product existingProduct = productService.findById(id);

            if (existingProduct == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
            }
            Product updatedProduct = productService.update(existingProduct, productJson, file);

            if (updatedProduct == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre ingresado ya existe");
            }

            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el producto: " + e.getMessage());
        }
    }
    
}
