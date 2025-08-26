package com.franciscoleon.blog.blog_francisco_leon.repository;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas.PostSummaryDTO(" +
            "p.id, p.title, p.content, p.createdAt, p.updatedAt) " +
           "FROM User u LEFT JOIN u.posts p WHERE u.id = :userId")
    List<PostSummaryDTO> findPostsLightByUserId(Long userId);

    @Query("""
        SELECT u FROM User u
        LEFT JOIN FETCH u.posts p
    """)
    List<User> findAllUsersWithPosts();
}
