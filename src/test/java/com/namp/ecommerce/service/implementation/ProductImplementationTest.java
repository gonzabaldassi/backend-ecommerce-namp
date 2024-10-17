package com.namp.ecommerce.service.implementation;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.ProductDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.mapper.MapperCategory;
import com.namp.ecommerce.mapper.MapperProduct;
import com.namp.ecommerce.mapper.MapperSubcategory;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.model.Product;
import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.repository.IProductDAO;
import com.namp.ecommerce.repository.ISubcategoryDAO;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.nio.file.Path;
import java.nio.file.Files;

public class ProductImplementationTest {

    @Mock
    private IProductDAO productDAO;

    @Mock
    private MapperCategory mapperCategory;

    @Mock
    private MapperSubcategory mapperSubcategory;

    @Mock
    private ISubcategoryDAO subcategoryDAO;

    @Mock
    private MapperProduct mapperProduct;

    @Mock
    private ObjectMapper objectMapper;



    @InjectMocks
    private ProductImplementation productService;
    private MockMultipartFile imageFile;

    @TempDir
    Path tempDir;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        imageFile = new MockMultipartFile("image", "test.jpg", "image/jpeg", "dummy content".getBytes());
        
    }
    
    @Test
    void getProducts(){
        Category category1 = new Category(1L, "Bebidas Con Alcohol","Descriptioon Bebidas con alcohol", null);
        Subcategory subcategory1 = new Subcategory(1L, "Vinos", "Seleccion de Vinos", category1, null);
        Product product1= new Product(1L,"Vino Toro Caja","Description Vino Toro",1500.0,20,"Imagen",subcategory1,null);
        Product product2= new Product(2L,"Vino Toro Botella","Description Vino Toro",2500.0,10,"Imagen",subcategory1,null);
        List<Product> products = Arrays.asList(product1,product2);

        when(productDAO.findAll()).thenReturn(products);


         
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Seleccion de Vinos", categoryDTO);
        ProductDTO product1DTO = new ProductDTO(1L, "Vino Toro Caja", "Description Vino Toro", 1500.0, 20, "Imagen", subcategoryDTO);
        ProductDTO product2DTO = new ProductDTO(2L, "Vino Toro Botella", "Description Vino Toro", 2500.0, 10, "Imagen", subcategoryDTO);

        when(mapperCategory.convertCategoryToDto(category1)).thenReturn(categoryDTO);
        when(mapperSubcategory.convertSubcategoryToDto(subcategory1)).thenReturn(subcategoryDTO);
        when(mapperProduct.convertProductToDto(product1)).thenReturn(product1DTO); 
        when(mapperProduct.convertProductToDto(product2)).thenReturn(product2DTO);

        List<ProductDTO> productsResult = productService.getProducts();

        assertEquals(2, productsResult.size(), "There should be 2 products in the result");
        assertEquals("Vino Toro Caja", productsResult.get(0).getName(), "The name of the first product should be Vino Toro Caja");
        assertEquals("Vino Toro Botella", productsResult.get(1).getName(), "The name of the second category should be Vino Toro Botella");

        verify(productDAO).findAll();
        verify(mapperProduct).convertProductToDto(product1);
        verify(mapperProduct).convertProductToDto(product2);
    }

    
    @Test
    void getProducts_EmptyList() {
        when(productDAO.findAll()).thenReturn(Collections.emptyList());

        List<ProductDTO> productsResult = productService.getProducts();

        assertTrue(productsResult.isEmpty(), "The list should be empty when there are no categories");

        verify(productDAO).findAll();
    }

    @Test
    void save() throws IOException {
        // Establecer el directorio de subida
        ReflectionTestUtils.setField(productService, "uploadDir", "src/test/resources/uploads");

        // Crear DTO de entrada
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Seleccion de Vinos", categoryDTO);
        ProductDTO savedProductDTO = new ProductDTO(1L, "VINO TORO CAJA", "Description Vino Toro", 1500.0, 20, "imagen", subcategoryDTO);

        // Crear entidad correspondiente
        Category category = new Category(1L, "Bebidas Con Alcohol", "Descripcion Bebidas con alcohol", new ArrayList<>());
        Subcategory subcategory = new Subcategory(1L, "Vinos", "Seleccion de Vinos", category, new ArrayList<>());
        Product product = new Product(1L, "VINO TORO CAJA", "Description Vino Toro", 1500.0, 20, "imagen", subcategory, new ArrayList<>());

        // Simular que no existe otro objeto productDTO con el mismo nombre
        when(productDAO.findAll()).thenReturn(List.of());

        // Mapper
        when(mapperProduct.convertDtoToProduct(any(ProductDTO.class))).thenReturn(product);
        when(productDAO.save(product)).thenReturn(product);
        when(mapperProduct.convertProductToDto(product)).thenReturn(savedProductDTO);

        // Simular el archivo de imagen
        MockMultipartFile file = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "Contenido de la imagen".getBytes());
        String productJson = "{ \"name\": \"Vino Toro Caja\",\"description\":\"Description Vino Toro\", \"price\": 1500.0 ,\"stock\":20, \"idSubcategory\":{\"idSubcategory\":1}}";

        // Ejecutar el método a probar
        ProductDTO productResult = productService.save(productJson, file);
        
        // Aserciones
        assertNotNull(productResult);
        assertEquals("VINO TORO CAJA", productResult.getName());

        // Verificaciones
        verify(productDAO).save(product);
        verify(productDAO).findAll();
        verify(mapperProduct).convertDtoToProduct(any(ProductDTO.class)); 
        verify(mapperProduct).convertProductToDto(product);
        
    }
    
    @Test
    void save_ExistingName() throws IOException{
        ReflectionTestUtils.setField(productService, "uploadDir", "src/test/resources/uploads");
       
        MockMultipartFile file = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "Contenido de la imagen".getBytes());
        String productJson = "{ \"name\": \"Vino Toro Botella\",\"description\":\"Description Vino Toro\", \"price\": 1500.0 ,\"stock\":20, \"idSubcategory\":{\"idSubcategory\":1}}";

        Product product = new Product();
        product.setName("VINO TORO BOTELLA");

        // Simulo que no existe otro objeto productDTO con el mismo nombre
        when(productDAO.findAll()).thenReturn(Collections.singletonList(product));

        ProductDTO productResult = productService.save(productJson,file);

        // Se verifica que sea null, es decir, la categoria no se guardo porque ya existe.
        assertNull(productResult);

        // Verifico que no se invoque save() ya que existe una categoria.
        verify(productDAO, never()).save(any(Product.class));
    }

    @Test
    void save_NameEmpty() throws IOException{
        ReflectionTestUtils.setField(productService, "uploadDir", "src/test/resources/uploads");

        MockMultipartFile file = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "Contenido de la imagen".getBytes());
        String productJson = "{ \"name\": \"\",\"description\":\"Description Vino Toro\", \"price\": 1500.0 ,\"stock\":20, \"idSubcategory\":{\"idSubcategory\":1}}";

        when(productService.save(productJson,file)).thenReturn(null);

        ProductDTO productResult = productService.save(productJson,file);


        // Se verifica que sea null, es decir, la categoria no se guardo porque es null.
        assertNull(productResult);

        // Verifico que no se invoque save() ya que el nombre esta vacio
        verify(productDAO, never()).save(any(Product.class));
    }

    @Test
    void save_DescriptionEmpty() throws IOException{
        ReflectionTestUtils.setField(productService, "uploadDir", "src/test/resources/uploads");

        MockMultipartFile file = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "Contenido de la imagen".getBytes());
        
        String productJson = "{ \"name\": \"Vino Toro Botella\",\"description\":\"\", \"price\": 1500.0 ,\"stock\":20, \"idSubcategory\":{\"idSubcategory\":1}}";


        when(productService.save(productJson,file)).thenReturn(null);

        ProductDTO productResult= productService.save(productJson,file);

        assertNull(productResult);

        // Verifico que no se invoque save() ya que la descripcion esta vacia
        verify(productDAO, never()).save(any(Product.class));
    }

    @Test
    void update() throws IOException{
        ReflectionTestUtils.setField(productService, "uploadDir", "src/test/resources/uploads");

        // Objeto que simula estar ya almacenado en la base de datos
        Category existingCategory = new Category(1L, "Bebidas con alcohol", "Description bebidas con alcohol", null);
        Subcategory existingSubcategory = new Subcategory(1L, "Vino", "Description vino", existingCategory,new ArrayList<>());
        Product existingProduct = new Product(1L,"Vino Toro Botella","Description Vino Toro", 1500.0, 20, "imagen", existingSubcategory, new ArrayList<>());

        // DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con Alcohol", "Description Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vino", "Description vino", categoryDTO);
        ProductDTO productDTO = new ProductDTO(1L,"Vino Nativo Caja","Description Vino Nativo",2500.0,40,"imagen2",subcategoryDTO);

        // Simulo que el id del objetoDTO ya se encuentra en la base de datos
        when(productDAO.findByIdProduct(productDTO.getIdProduct())).thenReturn(existingProduct);

        // Simulo la categoría actualizada
        Category updatedCategory = new Category(1L, "BEBIDAS CON ALCOHOL", "Description Bebidas con alcohol", null);
        Subcategory updatedSubcategory = new Subcategory(1L, "VINOS ACTUALIZADOS", "Description vino", updatedCategory, new ArrayList<>());
        Product updatedProduct = new Product(1L, "VINO NATIVO CAJA", "Description Vino Nativo",2500.0,40,"imagen2",updatedSubcategory, new ArrayList<>());

        when(productDAO.save(existingProduct)).thenReturn(updatedProduct);

        // Mappeo
        CategoryDTO updatedCategoryDTO = new CategoryDTO(1L,"BEBIDAS CON ALCOHOL", "Description Bebidas con alcohol");
        SubcategoryDTO updatedSubcategoryDTO = new SubcategoryDTO(1L, "VINOS ACTUALIZADOS", "Description vino", updatedCategoryDTO);
        ProductDTO updatedProductDTO = new ProductDTO(1L, "VINO NATIVO CAJA", "Description Vino Nativo",2500.0,40,"imagen2",updatedSubcategoryDTO);
        when(mapperProduct.convertProductToDto(any(Product.class))).thenReturn(updatedProductDTO);

        //JSON
        String existingProductJson = "{ \"name\": \"Vino Toro Caja\",\"description\":\"Description Vino Toro\", \"price\": 1500.0 ,\"stock\":20, \"idSubcategory\":{\"idSubcategory\":1}}";

        //Archivo Nuevo 
        MockMultipartFile newFile = new MockMultipartFile("file", "imagen.jpg", "image/jpeg", "Contenido de la imagen".getBytes());

        ProductDTO productResult = productService.update(productDTO, existingProductJson,newFile);

        assertNotNull(productResult);
        assertEquals("VINO NATIVO CAJA", productResult.getName());

        verify(productDAO).findByIdProduct(productDTO.getIdProduct());
        verify(productDAO).save(any(Product.class));
        
    }

    @Test
    void delete() throws IOException{
        String simulatedImgPath = "/images/imagen.png";
        productService.setSkipFileDeletion(true);
        // Simular Categoria y Subcategoria
        Category category = new Category(1L, "Bebidas Con alcohol", "Description de Bebidas con alcohol", null);
        Subcategory subcategory = new Subcategory(1L, "Vinos", "Vinos", category, new ArrayList<>());
        Product product = new Product(1L,"Vino Toro Caja","Description Vino Toro", 1500.0, 20, simulatedImgPath, subcategory, new ArrayList<>());

        // Simular DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Bebidas Con alcohol", "Description de Bebidas con alcohol");
        SubcategoryDTO subcategoryDTO = new SubcategoryDTO(1L, "Vinos", "Seleccion de Vinos", categoryDTO);
        ProductDTO productDTO = new ProductDTO(1L,"Vino Toro Caja","Description Vino Toro", 1500.0, 20, simulatedImgPath, subcategoryDTO);
        
        when(productDAO.findByIdProduct(productDTO.getIdProduct())).thenReturn(product);
        

        // Invocamos el Delete del Service 
        productService.delete(productDTO);

        // Verificar
        verify(productDAO).findByIdProduct(productDTO.getIdProduct());

        // Se verifica el metodo pero no se simula (when) ya que delete no retorna nada (void).
        verify(productDAO).delete(product);
    }

    @Test
    void findById() {
        
        // Definir una subcategoría
        Product product = new Product(1L,"Vino Toro Caja","Description Vino Toro", 1500.0, 20, "imagen", new Subcategory(), new ArrayList<>());
        
        ProductDTO productDTO= new ProductDTO(1L,"Vino Toro Caja","Description Vino Toro", 1500.0, 20, "imagen", null);


        // Simular que el DAO devuelve la subcategoría cuando se busca por ID
        when(productDAO.findByIdProduct(1L)).thenReturn(product);
        when(mapperProduct.convertProductToDto(product)).thenReturn(productDTO);

        // Ejecutar el método de servicio
        ProductDTO productResult = productService.findById(product.getIdProduct());

        // Verificar 
        assertEquals(1L, productResult.getIdProduct(), "The subcategory returned should match the expected one");
        assertEquals("Vino Toro Caja", productResult.getName());
        assertEquals("Description Vino Toro", productResult.getDescription());
        verify(productDAO).findByIdProduct(1L);
        verify(mapperProduct).convertProductToDto(product); 
    }



    @Test
    void verifyName() {
        String normalizedName = "VINOS TORO";
        //Simular Categoria y Subcategoria 
        Category category = new Category(1L, "Bebidas Con Alcohol", "Descripción Bebidas con alcohol", new ArrayList<>());
        Subcategory subcategory = new Subcategory(1L, "Vinos", "Selección de Vinos", category, new ArrayList<>());
        Product product1 = new Product(1L,"Vino Toro Caja","Description Vino Toro", 1500.0, 20, "imagen", subcategory, new ArrayList<>());
        Product product2 = new Product(2L,"Vino Toro Botella","Description Vino Toro Botella", 2500.0, 20, "imagen", subcategory, new ArrayList<>());
        List<Product> products = new ArrayList<>();

        products.add(product1);
        products.add(product2);

        when(productDAO.findAll()).thenReturn(products); 

        boolean productsResult = productService.verifyName(normalizedName);
        assertFalse(productsResult);
        verify(productDAO).findAll(); 
    }


}
