package com.franciscoleon.blog.blog_francisco_leon.service;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.SimpleUserDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserPasswordDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO;

import java.util.List;

public interface UserService {

    /**
     * Obtiene todos los usuarios , usado para pruebas en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @return lista de usuarios
     */
    List<SimpleUserDTO> getAllUsersTEST();

    /**
     * Obtiene la contraseña de un usuario por su ID , usado para pruebas en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @param id ID del usuario
     * @return contraseña del usuario
     */
    UserPasswordDTO getUserPasswordTEST(Long id);

    /**
     * Obtiene todos los usuarios con sus posts usado para pruebas en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @return lista de usuarios con sus posts
     */

    List<UserWithPostsDTO> getUsersWithPosts();

    /**
     * CHAT GPT VOY A POR TI
     * 
     * @return lista de usuarios con sus posts
     */
    List<UserWithPostsDTO> getUsersWithPostsOptimized();

    /**
     * Obtiene todos los usuarios con sus posts en formato ligero usado para pruebas
     * en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @return lista de usuarios con sus posts en formato ligero
     */
    List<UserWithPostsDTO> getUsersWithPostsLight();


    /**
     * Obtiene todos los usuarios con sus posts en formato ligero V2 usando query
     * optimizada
     * 
     * @return lista de usuarios con sus posts en formato ligero
     */
    List<UserWithPostsDTO> getUsersWithPostsLightV2();


    /**
     * Obtiene el post de un usuario especifico
     * @param userId
     * @param postId
     * @return
     */
    PostSummaryDTO getUserPostLight(Long userId, Long postId);
}
