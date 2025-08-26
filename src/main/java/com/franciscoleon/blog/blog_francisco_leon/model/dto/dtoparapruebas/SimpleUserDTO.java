package com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas;


/**
 * DTO de usuario {@link com.franciscoleon.blog.blog_francisco_leon.model.dto.UserDTO UserDTO} 
 * más simplificado para hacer pruebas en {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController TestController}.
 */
public class SimpleUserDTO {
    private Long id;
    private String username;
    private String email;
    private String role;

    public SimpleUserDTO(Long id, String username, String email, String role) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.role = role;
    }

    // getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
