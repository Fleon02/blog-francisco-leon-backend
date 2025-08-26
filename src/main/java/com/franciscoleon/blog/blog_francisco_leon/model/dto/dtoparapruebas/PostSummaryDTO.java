package com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas;

import java.time.LocalDateTime;

/**
 *
 * DTO de post usado para incluir los post a {@link com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO UserWithPostsDTO}
 * para pruebas en {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController TestController}
 */
public class PostSummaryDTO {

    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostSummaryDTO(Long id, String title, String content, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // getters y setters

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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
