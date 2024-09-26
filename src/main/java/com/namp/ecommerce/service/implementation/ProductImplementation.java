package com.namp.ecommerce.service.implementation;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.namp.ecommerce.dto.ProductDTO;
import com.namp.ecommerce.dto.ProductWithItDTO;
import com.namp.ecommerce.mapper.MapperProduct;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.repository.ISubcategoryDAO;
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
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ProductImplementation implements IProductService{

    @Autowired
    private IProductDAO productDAO;

    @Autowired
    private ISubcategoryDAO subcategoryDAO;

    @Autowired
    private MapperProduct mapperProduct;

    @Value("${image.upload.dir}")
    private String uploadDir;

    private boolean skipFileDeletion = false;

    public void setSkipFileDeletion(boolean skipFileDeletion) {
        this.skipFileDeletion = skipFileDeletion;
    }
    

    @Override
    public List<ProductDTO> getProducts() {
        return productDAO.findAll()
                .stream()
                .map(mapperProduct::convertProductToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductWithItDTO> getProductsWithIt() {
        return productDAO.findAll()
                .stream()
                .map(mapperProduct::convertProductWithItToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDTO getProductsId(long id){
        Product product = productDAO.findByIdProduct(id);

        if (product == null){
            return null;
        }
        return mapperProduct.convertProductToDto(product);
    }

    @Override
    public ProductDTO save(String productJson, MultipartFile file) throws IOException {

        // Creo json a objeto
        ObjectMapper objectMapper = new ObjectMapper();
        ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
        Path filePath = null;

        if (!file.isEmpty()){
            String contentType = file.getContentType();

            // Corroboro que el tipo de contenido sea una imagen
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")){
                throw new InvalidFileFormatException("El formato del archivo no es válido. Solo se permiten archivos JPG o PNG.");
            }

            // Genero un nombre custom para la imagen usando el nombre del producto y la fecha actual
            String fileExtension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
            String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String fileName = productDTO.getName().replaceAll("\\s+", "_").trim() + "_" + formattedDate + fileExtension;


            // Crea la ruta del archivo, si esta creada actualiza, de lo contrario crea
            filePath = Paths.get(uploadDir, fileName);


            // Seteo ruta al atributo img de product
            productDTO.setImg("/images/" + fileName);

        }
        // Normalizar los espacios en blanco y convertir a mayúsculas
        String normalizedName = productDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();
        if(!verifyName(normalizedName)) {
            productDTO.setName(normalizedName);
            Product product = mapperProduct.convertDtoToProduct(productDTO);
            Product savedProduct = productDAO.save(product);

            // Guardo la imagen (si un archivo se llama igual en el path lo va a reemplazar)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);


            return mapperProduct.convertProductToDto(savedProduct);
        }

        return null;
    }

        @Override
        public ProductDTO update(ProductDTO existingProductDTO, String productJson, MultipartFile file) throws IOException{
            Path filePath = null;

            Product existingProduct = productDAO.findByIdProduct(existingProductDTO.getIdProduct());
            if (existingProduct == null){
                return null;
            }

            // Convierto json a objeto
            ObjectMapper objectMapper = new ObjectMapper();
            ProductDTO productDTO = objectMapper.readValue(productJson, ProductDTO.class);
            // Normalizar los espacios en blanco y convertir a mayúsculas
            String normalizedName = productDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

            //Verifica que el nombre esta disponible
            if(verifyName(normalizedName, existingProductDTO.getIdProduct())) {
                return null; //Si el nombre ya esta siendo utilizado
            }

            //Actualizar los campos en la entidad existente
            existingProduct.setName(normalizedName);
            existingProduct.setDescription(productDTO.getDescription());
            existingProduct.setPrice(productDTO.getPrice());
            existingProduct.setStock(productDTO.getStock());
            //Buscamos la instancia de subcategoria en base a la subcategoriaDTO que esta setteada en el productoDTO existente
            existingProduct.setIdSubcategory(subcategoryDAO.findByIdSubcategory(existingProductDTO.getIdSubcategory().getIdSubcategory()));

            //Hago la verificacion de imagen
            if (file != null && !file.isEmpty()){

                String contentType = file.getContentType();

                // Corroboro que el tipo de contenido sea una imagen
                if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")){
                    throw new InvalidFileFormatException("El formato del archivo no es válido. Solo se permiten archivos JPG o PNG.");
                }

                // Genero un nombre custom para la imagen usando el nombre del producto y un UUID
                String fileExtension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
                String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String fileName = productDTO.getName().replaceAll("\\s+", "_").trim() + "_" + formattedDate + fileExtension;

                // Crea la ruta del archivo, si esta creada actualiza, de lo contrario crea
                filePath = Paths.get(uploadDir, fileName);

                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                existingProduct.setImg("/images/" + fileName);
            }

            //Guardamos el producto actualizado
            Product updatedProduct = productDAO.save(existingProduct);

            //Devolvemos el DTO del producto actualizado
            return mapperProduct.convertProductToDto(updatedProduct);
        }

    @Override
    public void delete(ProductDTO productDTO){
        Product product = productDAO.findByIdProduct(productDTO.getIdProduct());
        if (product == null){
            throw new EntityNotFoundException("Product not found with ID: " + productDTO.getIdProduct());
        }
        if (!skipFileDeletion) {
            String imgPathDto = productDTO.getImg().replace("/images","");
            //Eliminar la imagen asociada con ese producto
            String imgPath = uploadDir + imgPathDto;
            // Creo el objeto Path para el archivo de la imagen
            Path filePath = Paths.get(imgPath);
            try {
                Files.delete(filePath);
            } catch (IOException e) {
                throw new RuntimeException(product.getName(), e);
            }
        }
        // Luego elimino el objeto producto de la base de datos
        productDAO.delete(product);
    }

    @Override
    public ProductDTO findById(long id) {
        Product product = productDAO.findByIdProduct(id);
        if (product != null){
            return mapperProduct.convertProductToDto(product);
        }
        return null;
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
