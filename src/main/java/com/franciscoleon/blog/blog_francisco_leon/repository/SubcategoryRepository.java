package com.franciscoleon.blog.blog_francisco_leon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Subcategory;

@Repository
public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    /**
     * Encuentra todas las subcategorías en la base de datos y las devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @return una lista de SubcategoryDTO
     */
    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO(s.id, s.name) FROM Subcategory s ORDER BY s.id")
    List<SubcategoryDTO> findAllDTOTest();

    /**
     * Encuentra todas las subcategorías para una categoría dada en la base de datos y las
     * devuelve como una lista de DTO.
     * 
     * @param categoryId el id de la categoría
     * @return una lista de SubcategoryDTO
     */
    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO(s.id, s.name) " +
            "FROM Subcategory s WHERE s.category.id = :categoryId ORDER BY s.id")
    List<SubcategoryDTO> findByCategoryId(Long categoryId);

}
