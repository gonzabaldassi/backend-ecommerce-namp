package com.namp.ecommerce.service.implementation;

import com.namp.ecommerce.dto.CategoryDTO;
import com.namp.ecommerce.dto.CategoryWithSubcategoriesDTO;
import com.namp.ecommerce.dto.SubcategoryDTO;
import com.namp.ecommerce.mapper.MapperCategory;
import com.namp.ecommerce.model.Category;
import com.namp.ecommerce.model.Subcategory;
import com.namp.ecommerce.repository.ICategoryDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    void getCategories_Success() {
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
    void getCategoriesWithSubcategories_Success() {
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
    void getCategoriesIdWithSubcategories() {
    }

    @Test
    void save() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findById() {
    }

    @Test
    void verifyName() {
    }

    @Test
    void testVerifyName() {
    }
}