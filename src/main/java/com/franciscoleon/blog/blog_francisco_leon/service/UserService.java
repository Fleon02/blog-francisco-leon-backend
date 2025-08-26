package com.franciscoleon.blog.blog_francisco_leon.service;

import org.springframework.stereotype.Service;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.SimpleUserDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserPasswordDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Obtiene todos los usuarios , usado para pruebas en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @return lista de usuarios
     */
    public List<SimpleUserDTO> getAllUsersTEST() {
        return userRepository.findAll().stream()
                .map(user -> new SimpleUserDTO(
                        user.getId(),
                        user.getUsername(),
                        user.getEmail(),
                        user.getRole().name()))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene la contraseña de un usuario por su ID , usado para pruebas en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @param id ID del usuario
     * @return contraseña del usuario
     */
    public UserPasswordDTO getUserPasswordTEST(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserPasswordDTO(
                        user.getId(),
                        user.getPassword()))
                .orElse(null);
    }

    /**
     * Obtiene todos los usuarios con sus posts usado para pruebas en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @return lista de usuarios con sus posts
     */
    public List<UserWithPostsDTO> getUsersWithPosts() {
        return userRepository.findAll().stream().map(user -> {
            List<PostSummaryDTO> posts = user.getPosts().isEmpty()
                    ? List.of()
                    : user.getPosts().stream()
                            .map(p -> new PostSummaryDTO(p.getId(), p.getTitle(), p.getContent(), p.getCreatedAt(),
                                    p.getUpdatedAt()))
                            .toList();

            return new UserWithPostsDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name(),
                    user.getActive(),
                    posts.isEmpty() ? null : posts);
        }).toList();
    }

    /**
     * CHAT GPT VOY A POR TI
     * @return lista de usuarios con sus posts
     */
    public List<UserWithPostsDTO> getUsersWithPostsOptimized() {
        List<User> users = userRepository.findAllUsersWithPosts();

        return users.stream()
                .map(u -> {
                    List<PostSummaryDTO> posts = u.getPosts().stream()
                            .map(p -> new PostSummaryDTO(p.getId(), p.getTitle(), p.getContent(), p.getCreatedAt(),
                                    p.getUpdatedAt()))
                            .toList();

                    return new UserWithPostsDTO(
                            u.getId(),
                            u.getUsername(),
                            u.getEmail(),
                            u.getRole().name(),
                            u.getActive(),
                            posts.isEmpty() ? null : posts);
                })
                .toList();
    }

    /**
     * Obtiene todos los usuarios con sus posts en formato ligero usado para pruebas
     * en
     * {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController
     * TestController}
     * 
     * @return lista de usuarios con sus posts en formato ligero
     */
    public List<UserWithPostsDTO> getUsersWithPostsLight() {
        return userRepository.findAll().stream().map(user -> {
            List<PostSummaryDTO> posts = userRepository.findPostsLightByUserId(user.getId());
            return new UserWithPostsDTO(
                    user.getId(),
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole().name(),
                    user.getActive(),
                    posts.isEmpty() ? null : posts);
        }).toList();
    }

}
