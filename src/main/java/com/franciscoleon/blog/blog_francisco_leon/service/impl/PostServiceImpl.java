package com.franciscoleon.blog.blog_francisco_leon.service.impl;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.franciscoleon.blog.blog_francisco_leon.model.dto.CategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.ElementDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.PostCreateDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.dto.SubcategoryDTO;
import com.franciscoleon.blog.blog_francisco_leon.model.entities.Post;
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

        // Validar campos obligatorios
        if (dto.getUserId() == null) {
            throw new RuntimeException("El post debe tener un usuario");
        }

        if (dto.getCategoryId() == null && dto.getSubcategoryId() == null && dto.getElementId() == null) {
            throw new RuntimeException("El post debe tener al menos una categoría, subcategoría o elemento");
        }

        int count = 0;
        if (dto.getCategoryId() != null)
            count++;
        if (dto.getSubcategoryId() != null)
            count++;
        if (dto.getElementId() != null)
            count++;

        if (count > 1) {
            throw new RuntimeException(
                    "El post solo puede tener una categoría, subcategoría o elemento, no más de uno");
        }

        Post post = new Post();

        // Validar y asignar el usuario
        if (!userRepository.existsById(dto.getUserId())) {
            throw new RuntimeException("Usuario con ID " + dto.getUserId() + " no encontrado");
        }
        post.setUser(userRepository.getReferenceById(dto.getUserId()));

        // Validar y asignar categoría/subcategoría/elemento
        if (dto.getCategoryId() != null) {
            if (!categoryRepository.existsById(dto.getCategoryId())) {
                throw new RuntimeException("Categoría con ID " + dto.getCategoryId() + " no encontrada");
            }
            post.setCategory(categoryRepository.getReferenceById(dto.getCategoryId()));

        } else if (dto.getSubcategoryId() != null) {
            if (!subcategoryRepository.existsById(dto.getSubcategoryId())) {
                throw new RuntimeException("Subcategoría con ID " + dto.getSubcategoryId() + " no encontrada");
            }
            post.setSubcategory(subcategoryRepository.getReferenceById(dto.getSubcategoryId()));

        } else if (dto.getElementId() != null) {
            if (!elementRepository.existsById(dto.getElementId())) {
                throw new RuntimeException("Elemento con ID " + dto.getElementId() + " no encontrado");
            }
            post.setElement(elementRepository.getReferenceById(dto.getElementId()));
        }

        // Asignar contenido del post
        post.setTitle(dto.getTitle());
        post.setContent(dto.getContent());

        return postRepository.save(post);
    }

}
