package com.franciscoleon.blog.blog_francisco_leon.controller;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.CategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostCreateDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.SimpleUserDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserPasswordDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Post;
import com.franciscoleon.blog.blog_francisco_leon.service.PostService;
import com.franciscoleon.blog.blog_francisco_leon.service.UserService;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserService userService;
    private final PostService postService;

    public TestController(@Qualifier("userServiceV1") UserService userService,
            @Qualifier("postServiceV1") PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    private <T> ResponseEntity<Map<String, Object>> createResponse(HttpStatus status, String message, T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.status(status).body(body);
    }

    // ------------------ USUARIOS ------------------

    @GetMapping("/users")
    public ResponseEntity<Map<String, Object>> getAllUsers() {
        System.out.println("\nEntrando al endpoint: /users");
        List<SimpleUserDTO> users = userService.getAllUsersTEST();
        if (users.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron usuarios", List.of());
        }
        return createResponse(HttpStatus.OK, "Usuarios encontrados", users);
    }

    @GetMapping("/users/{id}/password")
    public ResponseEntity<Map<String, Object>> getUserPassword(@PathVariable Long id) {
        System.out.println("\nEntrando al endpoint: /users/" + id + "/password");
        UserPasswordDTO password = userService.getUserPasswordTEST(id);
        if (password == null) {
            return createResponse(HttpStatus.NOT_FOUND, "Usuario no encontrado", null);
        }
        return createResponse(HttpStatus.OK, "Contraseña obtenida", password);
    }

    @GetMapping("/users/userwithpassword")
    public ResponseEntity<Map<String, Object>> getUsersWithPasswords() {
        System.out.println("\nEntrando al endpoint: /users/userwithpassword");
        Map<Long, Map<String, Object>> result = new HashMap<>();
        userService.getAllUsersTEST().forEach(u -> {
            Map<String, Object> innerMap = new HashMap<>();
            innerMap.put("userInfo", u);
            innerMap.put("passwordInfo", userService.getUserPasswordTEST(u.getId()));
            result.put(u.getId(), innerMap);
        });

        if (result.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron usuarios con contraseñas", Map.of());
        }
        return createResponse(HttpStatus.OK, "Usuarios con contraseñas encontrados", result);
    }

    @GetMapping("/users/with-posts-light")
    public ResponseEntity<Map<String, Object>> getUsersWithPostsLight() {
        System.out.println("\nEntrando al endpoint: /users/with-posts-light");
        List<UserWithPostsDTO> users = userService.getUsersWithPostsLight();
        if (users.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron usuarios con posts (light)", List.of());
        }
        return createResponse(HttpStatus.OK, "Usuarios con posts (light) encontrados", users);
    }

    @GetMapping("/users/with-posts-raw")
    public ResponseEntity<Map<String, Object>> getUsersWithPostsRaw() {
        System.out.println("\nEntrando al endpoint: /users/with-posts-raw");
        List<UserWithPostsDTO> users = userService.getUsersWithPostsRaw();
        if (users.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron usuarios con posts (raw)", List.of());
        }
        return createResponse(HttpStatus.OK, "Usuarios con posts (raw) encontrados", users);
    }

    @GetMapping("/users/{userId}/posts-light/{postId}")
    public ResponseEntity<Map<String, Object>> getUserPostLight(
            @PathVariable Long userId,
            @PathVariable Long postId) {
        System.out.println("\nEntrando al endpoint: /users/" + userId + "/posts-light/" + postId);
        try {
            PostDTOLigero post = userService.getUserPostLight(userId, postId);
            if (post == null) {
                return createResponse(HttpStatus.NOT_FOUND,
                        "No se encontró el post " + postId + " para el usuario " + userId, null);
            }
            return createResponse(HttpStatus.OK, "Post encontrado", post);

        } catch (RuntimeException e) {
            return createResponse(HttpStatus.NOT_FOUND, e.getMessage(), null);
        }
    }

    // ------------------ CATEGORIAS, SUBCATEGORIAS Y ELEMENTOS ------------------

    @GetMapping("/categories")
    public ResponseEntity<Map<String, Object>> getCategories() {
        System.out.println("\nEntrando al endpoint: /categories");
        List<CategoryDTO> categories = postService.getAllCategories();
        if (categories.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron categorías", List.of());
        }
        return createResponse(HttpStatus.OK, "Categorías encontradas", categories);
    }

    @GetMapping("/subcategories")
    public ResponseEntity<Map<String, Object>> getSubcategories() {
        System.out.println("\nEntrando al endpoint: /subcategories");
        List<SubcategoryDTO> subcategories = postService.getAllSubcategories();
        if (subcategories.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron subcategorías", List.of());
        }
        return createResponse(HttpStatus.OK, "Subcategorías encontradas", subcategories);
    }

    @GetMapping("/elements")
    public ResponseEntity<Map<String, Object>> getElements() {
        System.out.println("\nEntrando al endpoint: /elements");
        List<ElementDTO> elements = postService.getAllElements();
        if (elements.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND, "No se encontraron elementos", List.of());
        }
        return createResponse(HttpStatus.OK, "Elementos encontrados", elements);
    }

    @GetMapping("/subcategories/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getSubcategoriesByCategory(@PathVariable Long categoryId) {
        System.out.println("\nEntrando al endpoint: /subcategories/category/" + categoryId);
        List<SubcategoryDTO> subcategories = postService.getSubcategoriesByCategory(categoryId);
        if (subcategories.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND,
                    "No se encontraron subcategorías para la categoría " + categoryId, List.of());
        }
        return createResponse(HttpStatus.OK, "Subcategorías encontradas", subcategories);
    }

    @GetMapping("/elements/subcategory/{subcategoryId}")
    public ResponseEntity<Map<String, Object>> getElementsBySubcategory(@PathVariable Long subcategoryId) {
        System.out.println("\nEntrando al endpoint: /elements/subcategory/" + subcategoryId);
        List<ElementDTO> elements = postService.getElementsBySubcategory(subcategoryId);
        if (elements.isEmpty()) {
            return createResponse(HttpStatus.NOT_FOUND,
                    "No se encontraron elementos para la subcategoría " + subcategoryId, List.of());
        }
        return createResponse(HttpStatus.OK, "Elementos encontrados", elements);
    }

    // CREAR POST

    @PostMapping("/create-post")
    public ResponseEntity<Map<String, Object>> createPost(@RequestBody PostCreateDTO dto) {
        System.out.println("\nEntrando al endpoint: /create-post");
        try {
            Post post = postService.createPost(dto);

            Map<String, Object> postResponse = new HashMap<>();
            postResponse.put("id", post.getId());
            postResponse.put("title", post.getTitle());
            postResponse.put("content", post.getContent());
            postResponse.put("createdAt", post.getCreatedAt());
            postResponse.put("updatedAt", post.getUpdatedAt());

            return createResponse(HttpStatus.CREATED, "Post creado correctamente", postResponse);

        } catch (RuntimeException e) {
            return createResponse(HttpStatus.BAD_REQUEST, e.getMessage(), null);
        }

    }
}