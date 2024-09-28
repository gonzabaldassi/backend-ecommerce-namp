package com.namp.ecommerce.service.implementation;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.mapper.MapperCategory;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.repository.ICategoryDAO;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryImplementationTest {

    @Mock
    private ICategoryDAO categoryDAO;

    @Mock
    private MapperCategory mapperCategory;

    @InjectMocks
    private CategoryImplementation categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getCategories() {
        Category category1 = new Category(1L, "Vino","Description Vino", null);
        Category category2 = new Category(2L, "Cerveza","Description Cerveza", null);
        List<Category> categories = Arrays.asList(category1, category2);

        // Simulo DAO
        when(categoryDAO.findAll()).thenReturn(categories);

        // Simulo Mapper
        when(mapperCategory.convertCategoryToDto(category1)).thenReturn(new CategoryDTO(1L, "Vino","Description Vino"));
        when(mapperCategory.convertCategoryToDto(category2)).thenReturn(new CategoryDTO(2L, "Cerveza","Description Cerveza"));

        // Resultado
        List<CategoryDTO> categoriesResult = categoryService.getCategories();

        assertEquals(2, categoriesResult.size(), "There should be 2 categories in the result");
        assertEquals("Vino", categoriesResult.get(0).getName(), "The name of the first category should be Vino");
        assertEquals("Cerveza", categoriesResult.get(1).getName(), "The name of the second category should be Cerveza");

        verify(categoryDAO).findAll();
        verify(mapperCategory).convertCategoryToDto(category1);
        verify(mapperCategory).convertCategoryToDto(category2);
    }

    @Test
    void getCategories_EmptyList() {
        when(categoryDAO.findAll()).thenReturn(Collections.emptyList());

        List<CategoryDTO> categoriesResult = categoryService.getCategories();

        assertTrue(categoriesResult.isEmpty(), "The list should be empty when there are no categories");

        verify(categoryDAO).findAll();
    }

    @Test
    void getCategoriesWithSubcategories() {
        Category category1 = new Category(1L, "Vino","Description Vino", new ArrayList<>());
        Subcategory subcategory1 = new Subcategory(1L, "Tinto", "Description Tinto", category1, null);
        Subcategory subcategory2 = new Subcategory(2L, "Blanco", "Description Blanco", category1, null);


        Category category2 = new Category(1L, "Cerveza","Description Cerveza", new ArrayList<>());
        Subcategory subcategory3 = new Subcategory(3L, "Rubia", "Description Rubia", category2, null);
        Subcategory subcategory4 = new Subcategory(4L, "Roja", "Description Roja", category2, null);

        category1.getSubcategories().add(subcategory1);
        category1.getSubcategories().add(subcategory2);
        category2.getSubcategories().add(subcategory3);
        category2.getSubcategories().add(subcategory4);

        List<Category> categories = Arrays.asList(category1, category2);

        // DTO
        SubcategoryDTO subcategoryDTO1 = new SubcategoryDTO(1L, "Tinto", "Description Tinto", null);
        SubcategoryDTO subcategoryDTO2 = new SubcategoryDTO(2L, "Blanco", "Description Blanco", null);
        List<SubcategoryDTO> category1SubcategoryDTO = Arrays.asList(subcategoryDTO1, subcategoryDTO2);

        SubcategoryDTO subcategoryDTO3 = new SubcategoryDTO(3L, "Rubia", "Description Rubia", null);
        SubcategoryDTO subcategoryDTO4 = new SubcategoryDTO(4L, "Roja", "Description Roja", null);
        List<SubcategoryDTO> category2SubcategoryDTO = Arrays.asList(subcategoryDTO3, subcategoryDTO4);

        CategoryWithSubcategoriesDTO categoryDTO1 = new CategoryWithSubcategoriesDTO(1L, "Vino","Description Vino", category1SubcategoryDTO, null);
        CategoryWithSubcategoriesDTO categoryDTO2 = new CategoryWithSubcategoriesDTO(2L, "Cerveza","Description Cerveza", category2SubcategoryDTO, null);

        // Simulo DAO
        when(categoryDAO.findAll()).thenReturn(categories);

        // Simulo Mapper
        when(mapperCategory.convertCategoryWithSubcategoryToDto(category1)).thenReturn(categoryDTO1);

        when(mapperCategory.convertCategoryWithSubcategoryToDto(category2)).thenReturn(categoryDTO2);

        // Resultado
        List<CategoryWithSubcategoriesDTO> categoriesResult = categoryService.getCategoriesWithSubcategories();

        assertEquals(2, categoriesResult.size(), "There should be 2 categories in the result");
        assertEquals("Vino", categoriesResult.get(0).getName(), "The name of the first category should be Vino");
        assertEquals("Tinto", categoriesResult.get(0).getSubcategories().get(0).getName(), "The name of the first subcategory should be Tinto");
        assertEquals("Blanco", categoriesResult.get(0).getSubcategories().get(1).getName(), "The name of the second subcategory should be Blanco");
        assertEquals("Cerveza", categoriesResult.get(1).getName(), "The name of the second category should be Cerveza");
        assertEquals("Rubia", categoriesResult.get(1).getSubcategories().get(0).getName(), "The name of the first subcategory should be Rubia");
        assertEquals("Roja", categoriesResult.get(1).getSubcategories().get(1).getName(), "The name of the second subcategory should be Roja");

        verify(categoryDAO).findAll();
        verify(mapperCategory).convertCategoryWithSubcategoryToDto(category1);
        verify(mapperCategory).convertCategoryWithSubcategoryToDto(category2);
    }

    @Test
    void getCategoriesWithSubcategories_ListEmpty() {
        // Simulo DAO
        when(categoryDAO.findAll()).thenReturn(Collections.emptyList());

        List<CategoryWithSubcategoriesDTO> categoriesResult = categoryService.getCategoriesWithSubcategories();

        assertTrue(categoriesResult.isEmpty(), "The list should be empty when there are no categories");

        verify(categoryDAO).findAll();

    }

    @Test
    void getCategoriesIdWithSubcategories() {
        Category category1 = new Category(1L, "Vino","Description Vino", new ArrayList<>());
        Subcategory subcategory1 = new Subcategory(1L, "Tinto", "Description Tinto", category1, null);
        Subcategory subcategory2 = new Subcategory(2L, "Blanco", "Description Blanco", category1, null);

        category1.getSubcategories().add(subcategory1);
        category1.getSubcategories().add(subcategory2);

        // DTO
        SubcategoryDTO subcategoryDTO1 = new SubcategoryDTO(1L, "Tinto", "Description Tinto", null);
        SubcategoryDTO subcategoryDTO2 = new SubcategoryDTO(2L, "Blanco", "Description Blanco", null);
        List<SubcategoryDTO> category1SubcategoryDTO = Arrays.asList(subcategoryDTO1, subcategoryDTO2);

        CategoryWithSubcategoriesDTO categoryDTO1 = new CategoryWithSubcategoriesDTO(1L, "Vino","Description Vino", category1SubcategoryDTO, null);

        // DAO
        when(categoryDAO.findByIdCategory(category1.getIdCategory())).thenReturn(category1);

        // Mapper
        when(mapperCategory.convertCategoryIdWithSubcategoryToDto(category1)).thenReturn(categoryDTO1);

        CategoryWithSubcategoriesDTO categoryResult = categoryService.getCategoriesIdWithSubcategories(1L);

        assertEquals(categoryDTO1, categoryResult, "The result should match the expected DTO");

        verify(categoryDAO).findByIdCategory(1L);
        verify(mapperCategory).convertCategoryIdWithSubcategoryToDto(category1);

    }

    @Test
    void getCategoriesIdWithSubcategories_ListEmpty() {
        long categoryId = 1L;

        // DAO
        when(categoryDAO.findByIdCategory(categoryId)).thenReturn(null);

        CategoryWithSubcategoriesDTO categoryResult = categoryService.getCategoriesIdWithSubcategories(categoryId);

        assertNull(categoryResult, "The result should be null when no category is found");

        verify(categoryDAO).findByIdCategory(categoryId);
    }

    @Test
    void save() {
        // DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Vino   tinto", "Description Vino");
        CategoryDTO savedCategoryDTO = new CategoryDTO(1L, "VINO TINTO", "Description Vino");

        // Entity
        Category category = new Category(1L, "VINO TINTO", "Description Vino", null);


        // Simulo que no existe otro objeto categoryDTO con el mismo nombre
        when(categoryDAO.findAll()).thenReturn(List.of());

        // Mapper
        when(mapperCategory.convertDtoToCategory(categoryDTO)).thenReturn(category);
        when(categoryDAO.save(category)).thenReturn(category);
        when(mapperCategory.convertCategoryToDto(category)).thenReturn(savedCategoryDTO);

        CategoryDTO categoryResult = categoryService.save(categoryDTO);

        assertNotNull(categoryResult);
        assertEquals("VINO TINTO", categoryResult.getName());

        verify(categoryDAO).save(category);
        verify(categoryDAO).findAll();
        verify(mapperCategory).convertDtoToCategory(categoryDTO);
        verify(mapperCategory).convertCategoryToDto(category);
    }

    @Test
    void save_ExistingName() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Vino");

        Category category = new Category();
        category.setName("VINO");

        // Simulo que no existe otro objeto categoryDTO con el mismo nombre
        when(categoryDAO.findAll()).thenReturn(Collections.singletonList(category));

        CategoryDTO categoryResult = categoryService.save(categoryDTO);

        // Se verifica que sea null, es decir, la categoria no se guardo porque ya existe.
        assertNull(categoryResult);

        // Verifico que no se invoque save() ya que existe una categoria.
        verify(categoryDAO, never()).save(any(Category.class));
    }

    @Test
    void save_NameEmpty() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("");

        when(categoryService.save(categoryDTO)).thenReturn(null);

        CategoryDTO categoryResult = categoryService.save(categoryDTO);


        // Se verifica que sea null, es decir, la categoria no se guardo porque es null.
        assertNull(categoryResult);

        // Verifico que no se invoque save() ya que el nombre esta vacio
        verify(categoryDAO, never()).save(any(Category.class));
    }

    @Test
    void save_NameAlpha() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Fernet1882");

        when(categoryService.save(categoryDTO)).thenReturn(null);

        CategoryDTO categoryResult = categoryService.save(categoryDTO);

        assertNull(categoryResult);

        // Verifico que no se invoque save() ya que el nombre no es alfabetico.
        verify(categoryDAO, never()).save(any(Category.class));
    }

    @Test
    void save_DescriptionEmpty() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("Vino");
        categoryDTO.setDescription("");

        when(categoryService.save(categoryDTO)).thenReturn(null);

        CategoryDTO categoryResult = categoryService.save(categoryDTO);

        assertNull(categoryResult);

        // Verifico que no se invoque save() ya que la descripcion esta vacia
        verify(categoryDAO, never()).save(any(Category.class));
    }

    @Test
    void update() {
        // Objeto que simula estar ya almacenado en la base de datos
        Category existingCategory = new Category(1L, "Vino", "Description vino", null);

        // DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Vino Reserva", "Description vino");

        // Simulo que el id del objetoDTO ya se encuentra en la base de datos
        when(categoryDAO.findByIdCategory(categoryDTO.getIdCategory())).thenReturn(existingCategory);

        // Simulo la categoría actualizada
        Category updatedCategory = new Category(1L, "VINO RESERVA", "Description vino", null);

        when(categoryDAO.save(existingCategory)).thenReturn(updatedCategory);

        // Mappeo
        CategoryDTO updatedCategoryDTO = new CategoryDTO(1L, "VINO RESERVA", "Description vino");
        when(mapperCategory.convertCategoryToDto(updatedCategory)).thenReturn(updatedCategoryDTO);

        CategoryDTO categoryResult = categoryService.update(categoryDTO, existingCategory);

        assertNotNull(categoryResult);
        assertEquals("VINO RESERVA", categoryResult.getName());

        verify(categoryDAO).findByIdCategory(categoryDTO.getIdCategory());
        verify(categoryDAO).save(existingCategory);
    }

    @Test
    void delete() {
        // Objeto que simula estar ya almacenado en la base de datos
        Category category = new Category(1L, "Vino", "Description vino", null);

        // DTO
        CategoryDTO categoryDTO = new CategoryDTO(1L, "Vino Reserva", "Description vino");

        when(categoryDAO.findByIdCategory(categoryDTO.getIdCategory())).thenReturn(category);

        categoryService.delete(categoryDTO);

        verify(categoryDAO).findByIdCategory(categoryDTO.getIdCategory());

        // Se verifica el metodo pero no se simula (when) ya que delete no retorna nada (void).
        verify(categoryDAO).delete(category);
    }

    @Test
    void findById() {
        long categoryId = 1L;

        Category category = new Category(categoryId, "Vino", "Description vino", null);

        CategoryDTO categoryDTO = new CategoryDTO(categoryId, "Vino", "Description vino");

        when(categoryDAO.findByIdCategory(categoryId)).thenReturn(category);
        when(mapperCategory.convertCategoryToDto(category)).thenReturn(categoryDTO);

        CategoryDTO categoryResult = categoryService.findById(categoryId);

        assertEquals(1L, categoryResult.getIdCategory());
        assertEquals("Vino", categoryResult.getName());
        assertEquals("Description vino", categoryResult.getDescription());

        verify(categoryDAO).findByIdCategory(categoryId);
        verify(mapperCategory).convertCategoryToDto(category);
    }

    @Test
    void verifyName() {
        String normalizeName = "VINO";

        Category category1 = new Category(1L, "Vino", "Description vino", null);
        Category category2 = new Category(2L, "Cerveza", "Description cerveza", null);
        List<Category> categories = new ArrayList<>();

        categories.add(category1);
        categories.add(category2);

        when(categoryDAO.findAll()).thenReturn(categories);

        boolean categoryResult = categoryService.verifyName(normalizeName);

        // Verifica que el resultado sea falso (no se encontro ningun objeto con el nombre)
        assertFalse(categoryResult);

        verify(categoryDAO).findAll();
    }


    @Test
    void normalizedName() {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setName("  Vino   nuevo  ");

        String normalizedName = categoryDTO.getName().replaceAll("\\s+", " ").trim().toUpperCase();

        assertEquals("VINO NUEVO", normalizedName);

    }
}