package com.franciscoleon.blog.blog_francisco_leon.controller;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.SimpleUserDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserPasswordDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO;
import com.franciscoleon.blog.blog_francisco_leon.service.UserService;

import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final UserService userService;

    public TestController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Obtiene todos los usuarios
     * 
     * @return lista de usuarios
     */
    @GetMapping("/users")
    public List<SimpleUserDTO> getAllUsers() {
        return userService.getAllUsersTEST();
    }

    /**
     * Obtiene la contraseña de un usuario por su ID
     * 
     * @param id ID del usuario
     * @return contraseña del usuario
     */
    @GetMapping("/users/{id}/password")
    public UserPasswordDTO getUserPassword(@PathVariable Long id) {
        return userService.getUserPasswordTEST(id);
    }

    /**
     * Obtiene todos los usuarios con sus contraseñas
     * 
     * @return mapa de usuarios con sus contraseñas
     */
    @GetMapping("/users/userwithpassword")
    public Map<Long, Map<String, Object>> getUsersWithPasswords() {
        Map<Long, Map<String, Object>> result = new HashMap<>();

        userService.getAllUsersTEST().forEach(u -> {
            Map<String, Object> innerMap = new HashMap<>();
            innerMap.put("userInfo", u);
            innerMap.put("passwordInfo", userService.getUserPasswordTEST(u.getId()));
            result.put(u.getId(), innerMap);
        });

        return result;
    }

    /**
     * Obtiene todos los usuarios con sus posts
     * 
     * @return lista de usuarios con sus posts
     */
    @GetMapping("/users/with-posts")
    public List<UserWithPostsDTO> getUsersWithPosts() {
        return userService.getUsersWithPosts();
    }

    /**
     * Obtiene todos los usuarios con sus posts en formato ligero
     * @return lista de usuarios con sus posts en formato ligero
     */
    @GetMapping("/users/with-posts-light")
    public List<UserWithPostsDTO> getUsersWithPostsLight() {
        return userService.getUsersWithPostsLight();
    }

        /**
     * METODO CHAT GPT PARA  VER SI TARDA MENOS
     * @return lista de usuarios con sus posts en formato ligero
     */
    @GetMapping("/users/with-posts-chatgpt")
    public List<UserWithPostsDTO> chatGPT() {
        return userService.getUsersWithPostsOptimized();
    }

}
