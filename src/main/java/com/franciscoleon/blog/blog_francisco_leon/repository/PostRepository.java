package com.franciscoleon.blog.blog_francisco_leon.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOVisual;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOLigero(" +
            "p.id, p.title, p.content, p.createdAt) " +
            "FROM User u INNER JOIN u.posts p WHERE u.id = :userId")
    List<PostDTOLigero> findPostsLightByUserId(@Param("userId") Long userId);

    @Query("SELECT new com.franciscoleon.blog.blog_francisco_leon.model.dto.PostDTOVisual(" +
            "p.id, p.title, p.content, p.createdAt, " +
            "c.name, s.name, e.name, " + // nombres
            "c.id, s.id, e.id) " + // ids
            "FROM Post p " +
            "LEFT JOIN p.category c " +
            "LEFT JOIN p.subcategory s " +
            "LEFT JOIN p.element e " +
            "WHERE p.id = :postId")
    PostDTOVisual findPostVisualById(@Param("postId") Long postId);

}
