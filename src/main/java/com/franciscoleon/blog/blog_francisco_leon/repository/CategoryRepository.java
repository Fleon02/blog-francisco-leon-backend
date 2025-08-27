package com.franciscoleon.blog.blog_francisco_leon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.CategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /**
     * Encuentra todas las categorias en la base de datos y las devuelve como una lista de DTO.
     * USADO PARA PRUEBAS
     * @return una lista de CategoryDTO
     */
    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.CategoryDTO(c.id, c.name) FROM Category c ORDER BY c.id")
    List<CategoryDTO> findAllDTOTest();

}
