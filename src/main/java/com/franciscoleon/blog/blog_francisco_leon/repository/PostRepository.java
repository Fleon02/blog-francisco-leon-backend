package com.franciscoleon.blog.blog_francisco_leon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Post;

public interface PostRepository extends JpaRepository<Post, Long> {
}
