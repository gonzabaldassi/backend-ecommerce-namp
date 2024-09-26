package com.namp.ecommerce.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.ProductDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.dto.SubcategoryWithProductsDTO;
import com.namp.ecommerce.mapper.MapperCategory;
import com.namp.ecommerce.mapper.MapperSubcategory;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.repository.ISubcategoryDAO;

import java.util.*;

public class SubCategoryImplmentationTest {
    
    @Mock
    private ISubcategoryDAO subcategoryDAO;

    @Mock
    private MapperCategory mapperCategory; 

    @Mock
    private MapperSubcategory mapperSubcategory; 

    @InjectMocks
    private SubcategoryImplementation subcategoryService; 

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getSubcategories_Success() {
    
        // Creamos Subcategorias con listas inicializadas
        Category category1 = new Category(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol", new ArrayList<>());
        Category category2 = new Category(2L, "Bebidas Sin Alcohol", "Descripción Bebidas sin alcohol", new ArrayList<>());        
    
        Subcategory subcategory1 = new Subcategory(1L, "Vinos", "Selección de Vinos", category1, new ArrayList<>());
        Subcategory subcategory2 = new Subcategory(2L, "Gaseosas", "Selección de gaseosas", category2, new ArrayList<>());
    
        // Añadir subcategorías a la lista de subcategorías de la categoría (opcional, si es necesario)
        category1.getSubcategories().add(subcategory1);
        category2.getSubcategories().add(subcategory2);
    
        List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2);
    
        // Simular el DAO
        when(subcategoryDAO.findAll()).thenReturn(subcategories);
    
        // Simular los DTOs
        CategoryDTO category1Dto = new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol");
        CategoryDTO category2Dto = new CategoryDTO(2L, "Bebidas Sin Alcohol", "Descripción Bebidas sin alcohol");
    
        SubcategoryDTO subcategory1Dto = new SubcategoryDTO(1L, "Vinos", "Selección de Vinos", category1Dto); 
        SubcategoryDTO subcategory2Dto = new SubcategoryDTO(2L, "Gaseosas", "Selección de gaseosas", category2Dto);
    
        // Simular el Mapper
        when(mapperCategory.convertCategoryToDto(category1)).thenReturn(category1Dto);
        when(mapperCategory.convertCategoryToDto(category2)).thenReturn(category2Dto);
        when(mapperSubcategory.convertSubcategoryToDto(subcategory1)).thenReturn(subcategory1Dto);
        when(mapperSubcategory.convertSubcategoryToDto(subcategory2)).thenReturn(subcategory2Dto);
    
        // Llamar al método del servicio
        List<SubcategoryDTO> subcategoriesResult = subcategoryService.getSubcategories();
    
        // Verificaciones
        assertEquals(2, subcategoriesResult.size(), "There should be 2 subcategories in the result");
        assertEquals("Vinos", subcategoriesResult.get(0).getName(), "The name of the first subcategory should be Vinos");
        assertEquals("Gaseosas", subcategoriesResult.get(1).getName(), "The name of the second subcategory should be Gaseosas");
    
        // Verificar interacciones con mocks
        verify(subcategoryDAO).findAll();
        verify(mapperSubcategory).convertSubcategoryToDto(subcategory1);
        verify(mapperSubcategory).convertSubcategoryToDto(subcategory2);
    }
    
    @Test
    void getSubcategories_EmpyList(){

        // Simula que la llamada a findAll()
        when(subcategoryDAO.findAll()).thenReturn(Collections.emptyList()); 
        List<SubcategoryDTO> subcategoriesResult = subcategoryService.getSubcategories();

        // Verificar
        assertTrue(subcategoriesResult.isEmpty(), "The list should be empty when there are no subcategories");
        verify(subcategoryDAO).findAll(); 
    }

    @Test
    void getSubcategoriesWithProducts_Success() {
        Category category1 = new Category(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol", new ArrayList<>());
        Subcategory subcategory1 = new Subcategory(1L, "Vinos", "Selección de Vinos", category1, new ArrayList<>());
        Product product1 = new Product(1L, "Toro Caja", "Vino TINTO CAJA TETRA", 1150, 300, "image1", subcategory1, new ArrayList<>());
        Product product2 = new Product(2L, "Malbec Botella", "Vino Malbec Botella", 2259, 400, "image2", subcategory1, new ArrayList<>());
        
        Category category2 = new Category(2L, "Bebidas Sin Alcohol", "Descripción Bebidas sin alcohol", new ArrayList<>());
        Subcategory subcategory2 = new Subcategory(2L, "Gaseosas", "Selección de Gaseosas", category2, new ArrayList<>());
        Product product3 = new Product(3L, "Coca Cola", "Coca Cola Classic", 2500, 500, "image3", subcategory2, new ArrayList<>());
    
        // Agregamos las subcategorías a las listas de las categorías
        category1.getSubcategories().add(subcategory1);
        category2.getSubcategories().add(subcategory2);
    
        // Agregamos los productos a las listas de productos de las subcategorías
        subcategory1.getProducts().add(product1);
        subcategory1.getProducts().add(product2);
        subcategory2.getProducts().add(product3);
    
        List<Subcategory> subcategories = Arrays.asList(subcategory1, subcategory2); 
    
        // Simulamos los DTOs
        ProductDTO product1Dto = new ProductDTO(1L, "Toro Caja", "Vino TINTO CAJA TETRA", 1150, 300, "", null);
        ProductDTO product2Dto = new ProductDTO(2L, "Malbec Botella", "Vino Malbec Botella", 2259, 400, "", null);
        List<ProductDTO> subcategory1ProductDTO = Arrays.asList(product1Dto, product2Dto);
    
        ProductDTO product3Dto = new ProductDTO(3L, "Coca Cola", "Coca Cola Classic", 2500, 500, "", null);
        List<ProductDTO> subcategory2ProductDTO = Arrays.asList(product3Dto);
    
        SubcategoryWithProductsDTO subcategoryDTO1 = new SubcategoryWithProductsDTO(1L, "Vinos", "Selección de Vinos", "Bebidas Con Alcohol", subcategory1ProductDTO);
        SubcategoryWithProductsDTO subcategoryDTO2 = new SubcategoryWithProductsDTO(2L, "Gaseosas", "Selección de Gaseosas", "Bebidas Sin Alcohol", subcategory2ProductDTO);
    
        // Simular el DAO
        when(subcategoryDAO.findAll()).thenReturn(subcategories);
    
        // Simular el Mapper
        when(mapperSubcategory.convertSubcategoryWithProductsToDto(subcategory1)).thenReturn(subcategoryDTO1);
        when(mapperSubcategory.convertSubcategoryWithProductsToDto(subcategory2)).thenReturn(subcategoryDTO2);
    
        // Ejecutamos el método del servicio
        List<SubcategoryWithProductsDTO> subcategoriesResult = subcategoryService.getSubcategoriesWithProducts();
    
        // Verificamos los resultados
        assertEquals(2, subcategoriesResult.size(), "There should be 2 subcategories in the result");
        assertEquals("Vinos", subcategoriesResult.get(0).getName(), "The name of the first subcategory should be Vinos");
        assertEquals("Toro Caja", subcategoriesResult.get(0).getProducts().get(0).getName(), "The name of the first product should be Toro Caja");
        assertEquals("Malbec Botella", subcategoriesResult.get(0).getProducts().get(1).getName(), "The name of the second product should be Malbec Botella");
        assertEquals("Gaseosas", subcategoriesResult.get(1).getName(), "The name of the second subcategory should be Gaseosas");
        assertEquals("Coca Cola", subcategoriesResult.get(1).getProducts().get(0).getName(), "The name of the first product in the second subcategory should be Coca Cola");
    
        // Verificamos las interacciones con los mocks
        verify(subcategoryDAO).findAll();
        verify(mapperSubcategory).convertSubcategoryWithProductsToDto(subcategory1);
        verify(mapperSubcategory).convertSubcategoryWithProductsToDto(subcategory2);
    }

    @Test
    void getSubcategoriesIdWithProducts_Success() {
        Category category1 = new Category(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol", new ArrayList<>());
        Subcategory subcategory1 = new Subcategory(1L, "Vinos", "Seleccion de Vinos", category1, new ArrayList<>());
        Product product1 = new Product(1L, "Toro Caja", "Vino TINTO CAJA TETRA", 1150, 300, "image1", subcategory1, new ArrayList<>());
        Product product2 = new Product(2L, "Malbec Botella", "Vino Malbec Botella", 2259, 400, "image2", subcategory1, new ArrayList<>());
        
        subcategory1.getProducts().add(product1);
        subcategory1.getProducts().add(product2);

        //DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L,"Vinos", "Seleccion de Vinos", categoryDTO );
        ProductDTO productDTO1 = new ProductDTO(1L, "Toro Caja", "Vino TINTO CAJA TETRA", 115, 300, "image1", subcategoryDTO );
        ProductDTO productDTO2 = new ProductDTO(2L, "Malbec Botella", "Vino Malbec Botella", 2259, 400, "image2", subcategoryDTO );
        List<ProductDTO> subcategory1ProductDTO = Arrays.asList(productDTO1, productDTO2);

        SubcategoryWithProductsDTO subcategoryDTO1 = new SubcategoryWithProductsDTO(1L, "Vinos", "Seleccion de Vinos", null, subcategory1ProductDTO);

        //DAO
        when(subcategoryDAO.findByIdSubcategory(subcategory1.getIdSubcategory())).thenReturn(subcategory1);

        //Mapper
        when(mapperSubcategory.convertSubcategoryIdWithProductsToDto(subcategory1)).thenReturn(subcategoryDTO1);

        SubcategoryWithProductsDTO subcategoryResult = subcategoryService.getSubcategoriesIdWithProducts(1L);
        
        //Verificar
        assertEquals(subcategoryDTO1, subcategoryResult);
        verify(subcategoryDAO).findByIdSubcategory(1L);
        verify(mapperSubcategory).convertSubcategoryIdWithProductsToDto(subcategory1);
    }
    
    @Test
    void save(){
        Category category1 = new Category(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol", null);
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Seleccion de Vinos", new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol"));
        Subcategory subcategory = new Subcategory(1L, "Vinos", "Seleccion de Vinos", category1, new ArrayList<>());
    
        // Simular el mapper para convertir el DTO a Subcategory
        when(mapperSubcategory.convertDtoToSubcategory(subcategoryDTO)).thenReturn(subcategory);
    
        // Simular el DAO para guardar Subcategory
        when(subcategoryDAO.save(subcategory)).thenReturn(subcategory);
    
        // Simular el mapper para convertir  Subcategory de nuevo a DTO
        when(mapperSubcategory.convertSubcategoryToDto(subcategory)).thenReturn(subcategoryDTO);
    
        SubcategoryDTO result = subcategoryService.save(subcategoryDTO);
    
        assertEquals("VINOS", result.getName(), "The name of the saved subcategory should be Vinos");
    
    
        verify(mapperSubcategory).convertDtoToSubcategory(subcategoryDTO);
        verify(subcategoryDAO).save(subcategory);
        verify(mapperSubcategory).convertSubcategoryToDto(subcategory);
    }
    
    @Test
    void update() {

        //Simular el objeto existente
        Category existingCategory = new Category(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol", null);
        Subcategory existingSubcategory = new Subcategory(1L, "Vinos", "Seleccion de Vinos", existingCategory, new ArrayList<>());

        // Datos para actualizar
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos Actualizados", "Seleccion actualizada de Vinos", categoryDTO);

        // Simulo que el id del objetoDTO ya se encuentra en la base de datos
        when(subcategoryDAO.findByIdSubcategory(subcategoryDTO.getIdSubcategory())).thenReturn(existingSubcategory);

        //Simlar la Subcategoria actualizada 
        Category updatedCategory = new Category(1L, "BEBIDAS CON ALCOHOL", "Descripcion Bebidas con alcohol", null);
        Subcategory updatedSubcategory = new Subcategory(1L, "VINOS ACTUALIZADOS", "Seleccion actualizada de Vinos", updatedCategory, new ArrayList<>());

        when(subcategoryDAO.save(existingSubcategory)).thenReturn(updatedSubcategory);
       
        //Simular el mapper
        CategoryDTO updatedCategoryDTO = new CategoryDTO(1L,"BEBIDAS CON ALCOHOL", "Descripcion Bebidas con alcohol");
        SubcategoryDTO updatedSubcategoryDTO = new SubcategoryDTO(1L, "VINOS ACTUALIZADOS", "Seleccion actualizada de Vinos", updatedCategoryDTO);

        when(mapperSubcategory.convertSubcategoryToDto(updatedSubcategory)).thenReturn(updatedSubcategoryDTO);
        
        SubcategoryDTO subcategoryResult = subcategoryService.update(subcategoryDTO, existingSubcategory);

        assertNotNull(subcategoryResult);
        assertEquals("VINOS ACTUALIZADOS", subcategoryResult.getName());

        verify(subcategoryDAO).findByIdSubcategory(subcategoryDTO.getIdSubcategory());
        verify(subcategoryDAO).save(existingSubcategory);

    }

    @Test
    void delete() {

        // Simular Categoria y Subcategoria
        Category category = new Category(1L, "Bebidas Con alcohol", "Description de Bebidas con alcohol", null);
        Subcategory subcategory = new Subcategory(1L, "Vinos", "Seleccion de Vinos", category, new ArrayList<>());

        // Simular DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con alcohol", "Description de Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Seleccion de Vinos", categoryDTO);

        when(subcategoryDAO.findByIdSubcategory(subcategoryDTO.getIdSubcategory())).thenReturn(subcategory);
        
        // Invocamos el Delete del Service 
        subcategoryService.delete(subcategoryDTO);

        // Verificar
        verify(subcategoryDAO).findByIdSubcategory(subcategoryDTO.getIdSubcategory());

        // Se verifica el metodo pero no se simula (when) ya que delete no retorna nada (void).
        verify(subcategoryDAO).delete(subcategory);
       
    }
  
    @Test
    void findById() {

        // Definir una subcategoría
        Subcategory subcategory = new Subcategory(1L, "Vinos", "Seleccion de Vinos", new Category(), new ArrayList<>());
        
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Seleccion de Vinos", null);

        // Simular que el DAO devuelve la subcategoría cuando se busca por ID
        when(subcategoryDAO.findByIdSubcategory(1L)).thenReturn(subcategory);
        when(mapperSubcategory.convertSubcategoryToDto(subcategory)).thenReturn(subcategoryDTO);

        // Ejecutar el método de servicio
        SubcategoryDTO subcategoryResult = subcategoryService.findById(subcategory.getIdSubcategory());

        // Verificar 
        assertEquals(1L, subcategoryResult.getIdSubcategory(), "The subcategory returned should match the expected one");
        assertEquals("Vinos", subcategoryResult.getName());
        assertEquals("Seleccion de Vinos", subcategoryResult.getDescription());
        verify(subcategoryDAO).findByIdSubcategory(1L);
        verify(mapperSubcategory).convertSubcategoryToDto(subcategory); 
    }

    @Test
    void verifyName() { 
        //Simular Categoria y Subcategoria 
        Category category1 = new Category(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol", new ArrayList<>());
        Subcategory subcategory1 = new Subcategory(1L, "Vinos", "Selección de Vinos", category1, new ArrayList<>());
        Subcategory subcategory2 = new Subcategory(2L, "Cervezas", "Selección de Cervezas", category1, new ArrayList<>());
        List<Subcategory> subcategories = new ArrayList<>();

        subcategories.add(subcategory1);
        subcategories.add(subcategory2);

        when(subcategoryDAO.findAll()).thenReturn(subcategories); 

        boolean subcategoriesResult = subcategoryService.verifyName("VINOS");
        assertFalse(subcategoriesResult);
        verify(subcategoryDAO).findAll(); 
    }

    @Test
    void findById_NotFound() {
        
        // Simular que no se encuentra la subcategoría
        when(subcategoryDAO.findByIdSubcategory(999L)).thenReturn(null);

        // Intentar encontrar una subcategoría con un ID que no existe
        SubcategoryDTO subcategoryResult = subcategoryService.findById(999L);

        // Verificar que el resultado sea nulo
        assertNull(subcategoryResult, "El resultado debería ser nulo si la subcategoría no se encuentra");
        verify(subcategoryDAO).findByIdSubcategory(999L);
    }

    @Test
    void update_NonExistentSubcategory() {
        // DTO de subcategoría para actualizar
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(999L, "Vinos Actualizados", "Descripción actualizada", categoryDTO);

        // Simular que la subcategoría no existe
        when(subcategoryDAO.findByIdSubcategory(subcategoryDTO.getIdSubcategory())).thenReturn(null);

        // Intentar actualizar una subcategoría que no existe
        subcategoryService.update(subcategoryDTO, null);
    
        verify(subcategoryDAO).findByIdSubcategory(subcategoryDTO.getIdSubcategory());
    }

    @Test
    void save_DuplicateName() {
        // Simular categoría existente
        Category category1 = new Category(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol", new ArrayList<>());
        
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Descripción Vinos", new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol"));
        
        Subcategory existingSubcategory = new Subcategory(1L, "Vinos", "Descripción Vinos", category1, new ArrayList<>());

        // Simular que ya existe una subcategoría con el mismo nombre
        when(subcategoryDAO.findAll()).thenReturn(Collections.singletonList(existingSubcategory));

        // Intentar guardar una subcategoría con un nombre duplicado
        subcategoryService.save(subcategoryDTO);

        verify(subcategoryDAO).findAll();
    }

    @Test
    void save_NameEmpty() {
        // Crear un DTO de subcategoría con nombre vacío
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO();
        subcategoryDTO.setName(""); // Nombre vacío

        // No se debe guardar la subcategoría, por lo que se simula que el DAO devuelve null
        when(subcategoryDAO.save(any(Subcategory.class))).thenReturn(null);

        // Llamar al método save del servicio
        SubcategoryDTO subcategoryResult = subcategoryService.save(subcategoryDTO);

        // Verificar que el resultado sea nulo, es decir, que la subcategoría no se guardó
        assertNull(subcategoryResult, "The subcategory should not be saved when the name is empty");

        // Verificar que no se invoque save() en el DAO ya que el nombre está vacío
        verify(subcategoryDAO, never()).save(any(Subcategory.class));
    }
}
