package com.franciscoleon.blog.blog_francisco_leon.service;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.CategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostCreateDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Post;

import java.util.List;

public interface PostService {

    /**
     * Encuentra todas las categorias en la base de datos y las devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @return una lista de CategoryDTO
     */
    List<CategoryDTO> getAllCategories();

    /**
     * Encuentra todas las subcategorias en la base de datos y las devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @return una lista de SubcategoryDTO
     */
    List<SubcategoryDTO> getAllSubcategories();

    /**
     * Encuentra todos los elementos en la base de datos y los devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @return una lista de ElementDTO
     */
    List<ElementDTO> getAllElements();

    /**
     * Encuentra todas las subcategorias de una categoria en la base de datos y las devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @param categoryId ID de la categoria
     * @return una lista de SubcategoryDTO
     */
    List<SubcategoryDTO> getSubcategoriesByCategory(Long categoryId);

    /**
     * Encuentra todos los elementos de una subcategoria en la base de datos y los devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @param subcategoryId ID de la subcategoria
     * @return una lista de ElementDTO
     */
    List<ElementDTO> getElementsBySubcategory(Long subcategoryId);


    /**
     * Crea un nuevo post en la base de datos.
     * USADO PARA PRUEBAS
     * 
     * @param postCreateDTO DTO con la información del nuevo post
     * @return el post creado
     */
    Post createPost(PostCreateDTO postCreateDTO);
}
