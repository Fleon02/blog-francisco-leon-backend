package com.franciscoleon.blog.blog_francisco_leon.model.dto;

/**
 * DTO para la creación de un nuevo post.
 * Contiene la información necesaria para crear un post.
 * USADO PARA PRUEBAS POR AHORA
 */
public class PostCreateDTO {

    private Long userId;
    private Long categoryId;
    private Long subcategoryId;
    private Long elementId;
    private String title;
    private String content;

    // Getters y Setters
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getCategoryId() { return categoryId; }
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public Long getSubcategoryId() { return subcategoryId; }
    public void setSubcategoryId(Long subcategoryId) { this.subcategoryId = subcategoryId; }

    public Long getElementId() { return elementId; }
    public void setElementId(Long elementId) { this.elementId = elementId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}
