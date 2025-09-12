package com.franciscoleon.blog.blog_francisco_leon.model.dto;

import java.time.LocalDateTime;


/**
 * DTO para representar la información de una publicación (post) de manera visual.
 * Usada para mostrar los datos del post en el frontend.
 * Incluye detalles básicos como el título, contenido, fecha de creación y el nombre del contexto
 * (que puede ser una categoría, subcategoría o elemento).
 */
public class PostDTOVisual {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    private String contextName;  // nombre del category/subcategory/element
    private Long contextId;      // id del contexto
    private String contextType;  // "category", "subcategory", "element"

    public PostDTOVisual(Long id, String title, String content, LocalDateTime createdAt,
                         String categoryName, String subcategoryName, String elementName,
                         Long categoryId, Long subcategoryId, Long elementId) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;

        // Solo uno de los tres contextos estará presente en la BBDD
        if (elementName != null) {
            this.contextName = elementName;
            this.contextId = elementId;
            this.contextType = "element";
        } else if (subcategoryName != null) {
            this.contextName = subcategoryName;
            this.contextId = subcategoryId;
            this.contextType = "subcategory";
        } else if (categoryName != null) {
            this.contextName = categoryName;
            this.contextId = categoryId;
            this.contextType = "category";
        } else {
            this.contextName = "Sin categoría";
            this.contextId = null;
            this.contextType = null;
        }
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getContextName() {
        return contextName;
    }

    public Long getContextId() {
        return contextId;
    }

    public String getContextType() {
        return contextType;
    }
}
