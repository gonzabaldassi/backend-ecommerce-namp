package com.namp.ecommerce.service.implementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namp.ecommerce.dto.ProductDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.mapper.EntityDtoMapper;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.service.IProductService;
import com.namp.ecommerce.error.InvalidFileFormatException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductImplementation implements IProductService{

    @Autowired
    private IProductDAO productDAO;

    @Autowired
    private EntityDtoMapper entityDtoMapper;

    @Override
    public List<ProductDTO> getProducts() {
        return productDAO.findAll()
                .stream()
                .map(entityDtoMapper::convertProductToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Product save(String productJson, MultipartFile file) throws IOException {

        // Creo json a objeto
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);
        Path filePath = null;

        if (!file.isEmpty()){
            String contentType = file.getContentType();

            // Corroboro que el tipo de contenido sea una imagen
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")){
                throw new InvalidFileFormatException("El formato del archivo no es válido. Solo se permiten archivos JPG o PNG.");
            }

            // Genero un nombre custom para la imagen usando el nombre del producto y un UUID
            String fileExtension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
            String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String fileName = product.getName().replaceAll("\\s+", "_").trim() + "_" + formattedDate + fileExtension;

            // Path donde se guardan las imagenes
            String uploadDir = "src/main/resources/images/";
            // Crea la ruta del archivo, si esta creada actualiza, de lo contrario crea
            filePath = Paths.get(uploadDir, fileName);


            // Seteo ruta al atributo img de product
            product.setImg("/images/" + fileName);
        }
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = product.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)) {
            product.setName(normalizedName);

            // Guardo la imagen (si un archivo se llama igual en el path lo va a reemplazar)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return productDAO.save(product);
        }
        return null;
    }

    @Override
    public Product update(Product existingProduct, String productJson, MultipartFile file) throws IOException{
        Path filePath = null;

        // Convierto json a objeto
        ObjectMapper objectMapper = new ObjectMapper();
        Product product = objectMapper.readValue(productJson, Product.class);

        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = product.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(verifyName(normalizedName, existingProduct.getIdProduct())) {
            return null;
        }

        existingProduct.setName(normalizedName);
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setIdSubcategory(product.getIdSubcategory());

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
            filePath = Paths.get(uploadDir, fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
            // Seteo ruta al atributo img de product

            existingProduct.setImg("/images/" + fileName);
        }

        return productDAO.save(existingProduct);
    }

    @Override
    public void delete(Product product){

        //Eliminar la imagen asociada con ese producto
        String imgPath = "src/main/resources" + product.getImg();
        // Creo el objeto Path para el archivo de la imagen
        Path filePath = Paths.get(imgPath);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error al eliminar la imagen del producto: " + product.getName(), e);
        }
        // Luego elimino el objeto producto de la base de datos
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

    @Override
    public boolean verifyName(String normalizedName, long idProduct) {
        List<Product> products = productDAO.findAll();
        String name = normalizedName.replaceAll("\\s+", "");

        //Verifica si se repite el nombre en los demas productos, menos con el que se está actualizando
        for(Product product : products){
            if(product.getIdProduct()!=idProduct && name.equals(product.getName().replaceAll("\\s+", ""))){
                return true;
            }
        }

        return false;
    }
    
}
