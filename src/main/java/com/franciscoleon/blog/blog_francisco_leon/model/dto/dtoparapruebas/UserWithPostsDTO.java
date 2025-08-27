package com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas;

import java.util.List;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero;


/**
 *
 * DTO de usuario que incluye sus Post gracias a {@link com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO PostSummaryDTO}
 * para pruebas en {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController TestController}
 */
public class UserWithPostsDTO {

    private Long id;
    private String username;
    private String email;
    private List<PostDTOLigero> posts;

    // Constructor vacío
    public UserWithPostsDTO() {}

    // Constructor con parámetros
    public UserWithPostsDTO(Long id, String username, String email, List<PostDTOLigero> posts) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.posts = posts;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<PostDTOLigero> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDTOLigero> posts) {
        this.posts = posts;
    }
}