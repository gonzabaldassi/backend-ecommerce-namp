package com.namp.ecommerce.services.implementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namp.ecommerce.models.Product;
import com.namp.ecommerce.repositories.IProductDAO;
import com.namp.ecommerce.services.IProductService;
import com.namp.ecommerce.error.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductImplementation implements IProductService{

    @Autowired
    private IProductDAO productDAO;

    @Override
    public List<Product> getProducts() {
        return productDAO.findAll();
    }

    @Override
    public Product save(String productJson, MultipartFile file) throws IOException {

        // Creo json a objeto
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);

        if (!file.isEmpty()){
            String contentType = file.getContentType();

            // Corroboro que el tipo de contenido sea una imagen
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")){
                throw new InvalidFileFormatException("El formato del archivo no es válido. Solo se permiten archivos JPG o PNG.");
            }
            // Obtengo el nombre original del archivo
            String fileName = file.getOriginalFilename();
            // Path donde se guardan las imagenes
            String uploadDir = "src/main/resources/images/";
            // Crea la ruta del archivo, si esta creada actualiza, de lo contrario crea
            Path filePath = Paths.get(uploadDir, fileName);

            // Guardo la imagen (si un archivo se llama igual en el path lo va a reemplazar)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Seteo ruta al atributo img de product
            product.setImg("/images/" + fileName);
        }
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = product.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            product.setName(normalizedName);
            return productDAO.save(product);
        }
        return null;
    }

    @Override
    public Product update(Product existingProduct, Product product) {
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = product.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(verifyName(normalizedName)) {
            return null;
        }

        existingProduct.setName(normalizedName);
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setIdSubcategory(product.getIdSubcategory());

        return productDAO.save(existingProduct);
    }

    @Override
    public void delete(Product product) {
        productDAO.delete(product);
    }

    @Override
    public Product findById(long id) {
        return productDAO.findByIdProduct(id);
    }

    @Override
    public boolean verifyName(String normalizedName) {
        List<Product> products = productDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Comparar el nombre de la categoria que se quiere guardar, con todos los demas sin espacio para ver si es el mismo
        for(Product product : products){
            if(name.equals(product.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }
    
}
