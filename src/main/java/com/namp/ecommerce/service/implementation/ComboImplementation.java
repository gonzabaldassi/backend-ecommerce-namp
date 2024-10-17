package com.namp.ecommerce.service.implementation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namp.ecommerce.dto.ComboDTO;
import com.namp.ecommerce.dto.ComboWithItDTO;
import com.namp.ecommerce.dto.ProductComboDTO;
import com.namp.ecommerce.error.InvalidFileFormatException;
import com.namp.ecommerce.mapper.MapperCombo;
import com.namp.ecommerce.model.Combo;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.model.ProductCombo;
import com.namp.ecommerce.repository.IProductComboDAO;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.service.IComboService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.namp.ecommerce.repository.IComboDAO;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ComboImplementation implements IComboService{

    @Autowired
    private MapperCombo mapperCombo;

    @Autowired
    private IComboDAO comboDAO;

    @Autowired
    private IProductDAO productDAO;

    @Autowired
    private IProductComboDAO productComboDAO;

    @Value("${image.upload.dir}")
    private String uploadDir;

    @Override
    public List<ComboDTO> getCombos(){
        return comboDAO.findAll()
                .stream()
                .map(mapperCombo::convertComboToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ComboWithItDTO> getCombosWithIt(){
        return comboDAO.findAll()
                .stream()
                .map(mapperCombo::convertComboWithItToDto)
                .collect(Collectors.toList());
    }

    @Override
    public ComboDTO saveCombo(ComboDTO comboDTO){
        //Normalizar los espacios en blanco y convertir a mayusculas
        String normalizedName = comboDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if(!verifyName(normalizedName)){
            comboDTO.setName(normalizedName);

            Combo combo = mapperCombo.convertDtoToCombo(comboDTO);

            Combo savedCombo = comboDAO.save(combo);

            return mapperCombo.convertComboToDto(savedCombo);
        }
        return null;
    }

    @Override
    public ComboWithItDTO saveComboWithIt(String comboJson, MultipartFile file) throws IOException {

        //Creo json a objeto
        ObjectMapper objectMapper = new ObjectMapper();
        ComboWithItDTO comboWithItDTO = objectMapper.readValue(comboJson, ComboWithItDTO.class);
        Path filePath = null;

        if (!file.isEmpty()) {
            String contentType = file.getContentType();

            //Corroboro que el tipo de contenido sea una imagen
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new InvalidFileFormatException("El formato del archivo no es valido. Solo se permiten archivos JPG o PNG.");
            }

            //Genero un nombre custom para la imagen usando el nombre del producto y la fecha actual
            String fileExtension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
            String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String fileName = comboWithItDTO.getName().replaceAll("\\s+","_").trim() + "_" + formattedDate + fileExtension;

            //Crea la ruta del archivo
            filePath = Paths.get(uploadDir, fileName);

            //Seteo la ruta al atributo img de producto
            comboWithItDTO.setImg("/images/" + fileName);
        }

        //Normalizar los espacios en blanco y convertir a mayusculas
        String normalizedName = comboWithItDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        if (!verifyName(normalizedName)) {
            comboWithItDTO.setName(normalizedName);

            // Convertir ComboWithProductComboDTO a entidad Combo
            Combo combo = mapperCombo.convertDtoToComboWithIt(comboWithItDTO);

            // Guardar el combo en la base de datos
            Combo savedCombo = comboDAO.save(combo);

            // Guardo la imagen (si un archivo se llama igual en el path lo va a reemplazar)
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return mapperCombo.convertComboWithItToDto(savedCombo);
        }
        return null;
    }

    @Override
    public ComboWithItDTO update(ComboWithItDTO existingComboWithItDTO, String comboJson, MultipartFile file) throws IOException {
        Path filePath = null;

        Combo existingCombo = comboDAO.findByIdCombo(existingComboWithItDTO.getIdCombo());
        if (existingCombo == null) {
            return null;
        }

        //Convierto json a objeto
        ObjectMapper objectMapper = new ObjectMapper();
        ComboWithItDTO comboWithItDTO = objectMapper.readValue(comboJson, ComboWithItDTO.class);


        //Normalizar los espacios en blacno y convertir a mayusculas
        String normalizedName = comboWithItDTO.getName().replaceAll("\s+", " ").trim().toUpperCase();

        //Verifica que el nombre esta disponible
        if (!verifyName(normalizedName, existingComboWithItDTO.getIdCombo())) {
            return null; //Si el nombre ya esta siendo utilizado
        }

        //Actualizar los campos de la entidad existente
        existingCombo.setName(comboWithItDTO.getName());
        existingCombo.setDescription(comboWithItDTO.getDescription());
        existingCombo.setPrice(comboWithItDTO.getPrice());
        //Creamos una lista de productCombos para guardar los productCombos que se van a settear
        //en el nuevo combo
        List<ProductCombo> productComboList = new ArrayList<>();
        //Buscamos la instancia de productCombo en base al productComboDTO
        for (ProductComboDTO productComboDTO : comboWithItDTO.getProductCombo()) {
            ProductCombo productCombo = productComboDAO.findByIdProductCombo(productComboDTO.getIdProductCombo());
            if (productCombo == null) {
                throw new EntityNotFoundException("Product not found with ID: " + productComboDTO.getIdProductCombo());
            }
            productComboList.add(productCombo);
        }

        //Hago la verificacion de imagen
        if (file != null && !file.isEmpty()) {

            String contentType = file.getContentType();

            //Corrobor que el tipo de contenido sea una imagen
            if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                throw new InvalidFileFormatException("El formato del archivo no es válido. Solo se permiten archivos JPG o PNG.");
            }

            // Genero un nombre custom para la imagen usando el nombre del producto y un UUID
            String fileExtension = contentType.equals("image/jpeg") ? ".jpg" : ".png";
            String formattedDate = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String fileName = comboWithItDTO.getName().replaceAll("\\s+", "_").trim() + "_" + formattedDate + fileExtension;

            //Crea la ruta del archivo, si esta creada actualiza, de lo contrario crea
            filePath = Paths.get(uploadDir, fileName);

            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            existingCombo.setImg("/images/" + fileName);
        }

        //Guardamos el combo actualizado
        Combo updatedCombo = comboDAO.save(existingCombo);

        //Devolvemos el DTO del combo actualizado
        return mapperCombo.convertComboWithItToDto(updatedCombo);
    }

    @Override
    public void delete(ComboDTO comboDTO){
        Combo combo = comboDAO.findByIdCombo(comboDTO.getIdCombo());
        if (combo == null) {
            throw new EntityNotFoundException("Combo not found with ID: " + comboDTO.getIdCombo());
        }
        String imgPathDto = comboDTO.getImg().replace("/images","");
        //Eliminar la imagen asociada con ese producto
        String imgPath = uploadDir + imgPathDto;
        //Creo el objeto Path para el archivo de la imagen
        Path filePath = Paths.get(imgPath);
        try{
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(combo.getName(), e);
        }

        //Luego elimino el objeto combo de la base de datos
        comboDAO.delete(combo);
    }

    @Override
    public ComboDTO findById(Long id) {
        Combo combo = comboDAO.findByIdCombo(id);
        if (combo == null) {
            return null;
        }
        return mapperCombo.convertComboToDto(combo);
    }

    @Override
    public ComboWithItDTO findByIdWithIt(Long id) {
        Combo combo = comboDAO.findByIdCombo(id);
        if (combo == null) {
            return null;
        }
        return mapperCombo.convertComboWithItToDto(combo);
    }

    @Override
    public boolean verifyName(String normalizedName){
        List<Combo> combos = comboDAO.findAll();
        String name = normalizedName.replaceAll("\\s","");

        //Comparar el nombre del Combo que se quiere guardar, con todos los demas sin espacios
        for(Combo combo : combos){
            if(name.equals(combo.getName().replaceAll("\\s",""))){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean verifyName(String normalizedName, long comboId) {
        List<Combo> combos = comboDAO.findAll();
        String name = normalizedName.replaceAll("\\s","");

        //Comparar el nombre del Combo que se quiere guardar, con todos los demas sin espacios
        for(Combo combo : combos){
            if(combo.getIdCombo() != comboId && name.equals(combo.getName().replaceAll("\\s",""))){
                return true;
            }
        }
        return false;
    }
}

