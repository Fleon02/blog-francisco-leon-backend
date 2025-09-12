package com.franciscoleon.blog.blog_francisco_leon.repository;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

        User findByUsername(String username);

        User findByEmail(String email);

        boolean existsByUsername(String username);

        boolean existsByEmail(String email);


        @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero(" +
                        "p.id, p.title, p.content, p.createdAt) " +
                        "FROM User u LEFT JOIN u.posts p " +
                        "WHERE u.id = :userId AND p.id = :postId")
        PostDTOLigero findPostLightByUserIdAndPostId(Long userId, Long postId);

        @Query("""
                            SELECT DISTINCT u FROM User u
                            LEFT JOIN FETCH u.posts p
                        """)
        List<User> findAllUsersWithPostsLight();

        @Query("""
                            SELECT u.id, u.username, u.email, p.id, p.title, p.content, p.createdAt
                            FROM User u
                            LEFT JOIN u.posts p
                        """)
        List<Object[]> findAllUsersWithPostsLightRaw();

        @Query("""
                            SELECT u.id, u.username, u.email, p.id, p.title, p.content, p.createdAt
                            FROM User u
                            LEFT JOIN u.posts p
                            WHERE u.id = :userId
                        """)
        Object[] findUserWithPostsLightRaw(@Param("userId") Long userId);

}
