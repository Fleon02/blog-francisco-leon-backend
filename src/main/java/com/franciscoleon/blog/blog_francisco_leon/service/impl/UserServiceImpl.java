package com.franciscoleon.blog.blog_francisco_leon.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.SimpleUserDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserPasswordDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.repository.UserRepository;
import com.franciscoleon.blog.blog_francisco_leon.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("userServiceV1")
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;

        public UserServiceImpl(UserRepository userRepository) {
                this.userRepository = userRepository;
        }

        @Override
        @Transactional(readOnly = true)
        public List<SimpleUserDTO> getAllUsersTEST() {
                return userRepository.findAll().stream()
                                .map(user -> new SimpleUserDTO(
                                                user.getId(),
                                                user.getUsername(),
                                                user.getEmail(),
                                                user.getRole().name()))
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional(readOnly = true)
        public UserPasswordDTO getUserPasswordTEST(Long id) {
                return userRepository.findById(id)
                                .map(user -> new UserPasswordDTO(
                                                user.getId(),
                                                user.getPassword()))
                                .orElse(null);
        }

        @Override
        @Transactional(readOnly = true)
        public List<UserWithPostsDTO> getUsersWithPostsLight() {
                List<User> users = userRepository.findAllUsersWithPostsLight();

                return users.stream()
                                .map(user -> {
                                        List<PostDTOLigero> posts = user.getPosts().stream()
                                                        .map(p -> new PostDTOLigero(
                                                                        p.getId(),
                                                                        p.getTitle(),
                                                                        p.getContent(),
                                                                        p.getCreatedAt()))
                                                        .toList();

                                        return new UserWithPostsDTO(
                                                        user.getId(),
                                                        user.getUsername(),
                                                        user.getEmail(),
                                                        posts.isEmpty() ? null : posts);
                                })
                                .toList();
        }

        @Transactional(readOnly = true)
        public List<UserWithPostsDTO> getUsersWithPostsRaw() {
                List<Object[]> rawResults = userRepository.findAllUsersWithPostsLightRaw();

                // Agrupar por ID de usuario
                Map<Long, UserWithPostsDTO> userMap = new LinkedHashMap<>();

                for (Object[] row : rawResults) {
                        Long userId = (Long) row[0];
                        String username = (String) row[1];
                        String email = (String) row[2];
                        Long postId = (Long) row[3];
                        String postTitle = (String) row[4];
                        String postContent = (String) row[5];
                        LocalDateTime postCreatedAt = (LocalDateTime) row[6];

                        // Si el usuario no existe aún en el mapa, lo creamos
                        userMap.putIfAbsent(userId, new UserWithPostsDTO(userId, username, email, new ArrayList<>()));

                        // Si el post no es nulo, lo agregamos
                        if (postId != null) {
                                userMap.get(userId).getPosts().add(
                                                new PostDTOLigero(postId, postTitle, postContent, postCreatedAt));
                        }
                }

                return new ArrayList<>(userMap.values());
        }

        @Transactional(readOnly = true)
        public PostDTOLigero getUserPostLight(Long userId, Long postId) {

                PostDTOLigero post = userRepository.findPostLightByUserIdAndPostId(userId, postId);

                if (post == null) {
                        throw new RuntimeException("Usuario o post no encontrado");
                }

                return post;
        }

}
