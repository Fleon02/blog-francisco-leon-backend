package com.franciscoleon.blog.blog_francisco_leon.model.dto;

import java.time.LocalDateTime;

/**
 * DTO ligero para la representación de un Post. Usado para la solucion de GROK a ver si funciona
 */
public class PostDTOLigero {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;

    // Constructor vacío
    public PostDTOLigero() {}

    // Constructor con parámetros
    public PostDTOLigero(Long id, String title, String content, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
    }

    
    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
