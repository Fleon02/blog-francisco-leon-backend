package com.franciscoleon.blog.blog_francisco_leon.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.CategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostCreateDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Category;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Element;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Post;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Subcategory;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.repository.CategoryRepository;
import com.franciscoleon.blog.blog_francisco_leon.repository.ElementRepository;
import com.franciscoleon.blog.blog_francisco_leon.repository.PostRepository;
import com.franciscoleon.blog.blog_francisco_leon.repository.SubcategoryRepository;
import com.franciscoleon.blog.blog_francisco_leon.repository.UserRepository;
import com.franciscoleon.blog.blog_francisco_leon.service.PostService;

/**
 * Servicio para gestionar las operaciones relacionadas con las publicaciones.
 * POR AHORA TIENE CATEGORIAS, SUBCATEGORIAS Y ELEMENTOS PORQUE LOS POST LO
 * NECESITAN AL MENOS 1
 * ES POSIBLE QUE SE CAMBIE EN UN FUTURO Y CADA UNO TENGA SU SERVICE
 * POR PRUEBAS SE HIZO AQUI POR IR MÁS RÁPIDO
 */
@Service("postServiceV1")
public class PostServiceImpl implements PostService {

    private final CategoryRepository categoryRepository;

    private final SubcategoryRepository subcategoryRepository;

    private final ElementRepository elementRepository;

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostServiceImpl(CategoryRepository categoryRepository, SubcategoryRepository subcategoryRepository,
            ElementRepository elementRepository, PostRepository postRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.elementRepository = elementRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAllDTOTest();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoryDTO> getAllSubcategories() {
        return subcategoryRepository.findAllDTOTest();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElementDTO> getAllElements() {
        return elementRepository.findAllDTOTest();
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubcategoryDTO> getSubcategoriesByCategory(Long categoryId) {
        return subcategoryRepository.findByCategoryId(categoryId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ElementDTO> getElementsBySubcategory(Long subcategoryId) {
        return elementRepository.findBySubcategoryId(subcategoryId);
    }

    @Override
    @Transactional
    public Post createPost(PostCreateDTO dto) {


        if (dto.getUserId() == null) {
            throw new RuntimeException("El post debe tener un usuario");
        }

        if (dto.getCategoryId() == null && dto.getSubcategoryId() == null && dto.getElementId() == null) {
            throw new RuntimeException("El post debe tener al menos una categoría, subcategoría o elemento");
        }

        // Por ahora esto, a lo mejor en un futuro si se permite

        int count = 0;
        if (dto.getCategoryId() != null)
            count++;
        if (dto.getSubcategoryId() != null)
            count++;
        if (dto.getElementId() != null)
            count++;

        if (count > 1) {
            throw new RuntimeException("El post solo puede tener categoría, subcategoría o elemento, no más de uno");
        }

        Post post = new Post();

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        post.setUser(user);

        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
            post.setCategory(category);
        } else if (dto.getSubcategoryId() != null) {
            Subcategory subcategory = subcategoryRepository.findById(dto.getSubcategoryId())
                    .orElseThrow(() -> new RuntimeException("Subcategoría no encontrada"));
            post.setSubcategory(subcategory);
        } else if (dto.getElementId() != null) {
            Element element = elementRepository.findById(dto.getElementId())
                    .orElseThrow(() -> new RuntimeException("Elemento no encontrado"));
            post.setElement(element);
        }

        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return postRepository.save(post);
    }

}
