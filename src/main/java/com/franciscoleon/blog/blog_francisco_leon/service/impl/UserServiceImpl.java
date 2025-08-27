package com.franciscoleon.blog.blog_francisco_leon.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.SimpleUserDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserPasswordDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.UserWithPostsDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.repository.UserRepository;
import com.franciscoleon.blog.blog_francisco_leon.service.UserService;

import java.util.List;
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
        public List<UserWithPostsDTO> getUsersWithPosts() {
                return userRepository.findAll().stream().map(user -> {
                        List<PostSummaryDTO> posts = user.getPosts().isEmpty()
                                        ? List.of()
                                        : user.getPosts().stream()
                                                        .map(p -> new PostSummaryDTO(p.getId(), p.getTitle(),
                                                                        p.getContent(), p.getCreatedAt(),
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

        @Override
        @Transactional(readOnly = true)
        public List<UserWithPostsDTO> getUsersWithPostsOptimized() {
                List<User> users = userRepository.findAllUsersWithPosts();

                return users.stream()
                                .map(u -> {
                                        List<PostSummaryDTO> posts = u.getPosts().stream()
                                                        .map(p -> new PostSummaryDTO(p.getId(), p.getTitle(),
                                                                        p.getContent(), p.getCreatedAt(),
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

        @Override
        @Transactional(readOnly = true)
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

        @Override
        @Transactional(readOnly = true)
        public List<UserWithPostsDTO> getUsersWithPostsLightV2() {
                List<User> users = userRepository.findAllUsersWithPostsLight();

                return users.stream()
                                .map(user -> {
                                        List<PostSummaryDTO> posts = user.getPosts().stream()
                                                        .map(p -> new PostSummaryDTO(
                                                                        p.getId(),
                                                                        p.getTitle(),
                                                                        p.getContent(),
                                                                        p.getCreatedAt(),
                                                                        p.getUpdatedAt()))
                                                        .toList();

                                        return new UserWithPostsDTO(
                                                        user.getId(),
                                                        user.getUsername(),
                                                        user.getEmail(),
                                                        user.getRole().name(),
                                                        user.getActive(),
                                                        posts.isEmpty() ? null : posts);
                                })
                                .toList();
        }

        @Transactional(readOnly = true)
        public PostSummaryDTO getUserPostLight(Long userId, Long postId) {

                PostSummaryDTO post = userRepository.findPostLightByUserIdAndPostId(userId, postId);

                if (post == null) {
                        throw new RuntimeException("Usuario o post no encontrado");
                }

                return post;
        }

}
