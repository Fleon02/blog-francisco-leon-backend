package com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas;

import java.util.List;


/**
 *
 * DTO de usuario que incluye sus Post gracias a {@link com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO PostSummaryDTO}
 * para pruebas en {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController TestController}
 */
public class UserWithPostsDTO {

    private Long id;
    private String username;
    private String email;
    private String role;
    private Boolean active;
    private List<PostSummaryDTO> posts;

    public UserWithPostsDTO(Long id, String username, String email, String role, Boolean active, List<PostSummaryDTO> posts) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
        this.active = active;
        this.posts = posts;
    }

    // getters y setters

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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<PostSummaryDTO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostSummaryDTO> posts) {
        this.posts = posts;
    }
}
