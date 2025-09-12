package com.franciscoleon.blog.blog_francisco_leon.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.franciscoleon.blog.blog_francisco_leon.service.PostService;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(@Qualifier("postServiceV1") PostService postService) {
        this.postService = postService;
    }

    private <T> ResponseEntity<Map<String, Object>> createResponse(HttpStatus status, String message, T data) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("message", message);
        body.put("data", data);
        return ResponseEntity.status(status).body(body);
    }


    @GetMapping("getUserPosts/{email}")
    public ResponseEntity<Map<String, Object>> getPostOfUserByEmail(@PathVariable String email) {
        try {
            var posts = postService.getAllPostsOfUserByEmail(email);

            if (posts != null && !posts.isEmpty()) {
                return createResponse(HttpStatus.OK, "Posts encontrados", posts);
            } else {
                return createResponse(HttpStatus.NOT_FOUND, "No se encontraron posts", null);
            }
        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    @GetMapping("getPostVisual/{postId}")
    public ResponseEntity<Map<String, Object>> getPostVisualById(@PathVariable Long postId) {
        System.err.println("\n\n\npostId: " + postId);
        try {
            var post = postService.getPostVisualById(postId);

            if (post != null) {
                return createResponse(HttpStatus.OK, "Post encontrado", post);
            } else {
                return createResponse(HttpStatus.NOT_FOUND, "No se encontró el post", null);
            }
        } catch (Exception e) {
            return createResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
