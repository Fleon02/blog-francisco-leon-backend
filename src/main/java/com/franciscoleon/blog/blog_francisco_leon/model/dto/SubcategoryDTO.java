package com.franciscoleon.blog.blog_francisco_leon.model.dto;

public class SubcategoryDTO {
    private Long id;
    private String name;

    public SubcategoryDTO() {}

    public SubcategoryDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}
