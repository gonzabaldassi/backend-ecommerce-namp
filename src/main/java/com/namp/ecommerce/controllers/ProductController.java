package com.namp.ecommerce.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.namp.ecommerce.models.Product;
import com.namp.ecommerce.services.IProductService;

import jakarta.validation.Valid;

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
    public ResponseEntity<?> createProduct(@Valid @RequestBody Product product){
        try{
            Product createdProduct = productService.save(product);

            if (createdProduct == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Este producto ya se encuentra registrado");


            }

            return ResponseEntity.ok(createdProduct);
        }catch(Exception e){
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
    public ResponseEntity<?> updateProduct(@PathVariable long id, @Valid @RequestBody Product product){
        try{
            Product existingProduct = productService.findById(id);

            if (existingProduct == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El producto no existe");
            }
            Product updatedProduct = productService.update(existingProduct, product);

            if (updatedProduct == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El nombre ingresado ya existe");
            }

            return ResponseEntity.ok(updatedProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el producto: " + e.getMessage());
        }
    }
    
}
