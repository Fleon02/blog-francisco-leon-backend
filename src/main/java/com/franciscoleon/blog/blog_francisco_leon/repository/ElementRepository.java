package com.franciscoleon.blog.blog_francisco_leon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Element;

public interface ElementRepository extends JpaRepository<Element, Long> {

    /**
     * Encuentra todos los elementos en la base de datos y los devuelve como una
     * lista de DTO.
     * USADO PARA PRUEBAS
     * 
     * @return una lista de ElementDTO
     */
    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO(e.id, e.name) FROM Element e ORDER BY e.id")
    List<ElementDTO> findAllDTOTest();

    /**
     * Encuentra todos los elementos para una subcategor a dada en la base de datos y los
     * devuelve como una lista de DTO.
     * 
     * @param subcategoryId el id de la subcategor a
     * @return una lista de ElementDTO
     */
    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO(e.id, e.name) " +
            "FROM Element e WHERE e.subcategory.id = :subcategoryId ORDER BY e.id")
    List<ElementDTO> findBySubcategoryId(Long subcategoryId);

}
