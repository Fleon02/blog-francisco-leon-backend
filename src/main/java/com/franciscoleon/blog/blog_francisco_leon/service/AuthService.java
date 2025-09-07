package com.franciscoleon.blog.blog_francisco_leon.service;

import com.franciscoleon.blog.blog_francisco_leon.model.entities.User;
import com.franciscoleon.blog.blog_francisco_leon.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registrar un usuario nuevo.
     * @param username Nombre de usuario
     * @param email Correo electrónico
     * @param rawPassword Contraseña en texto plano
     * @return Usuario registrado
     */
    @Transactional
    public User register(String username, String email, String rawPassword) {
        if (userRepository.findByUsername(username) != null || userRepository.findByEmail(email) != null) {
            throw new RuntimeException("El nombre de usuario o el correo electrónico ya existen");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword)); // 🔑 hash con bcrypt
        user.setRole(User.Role.USER);
        user.setActive(true);

        return userRepository.save(user);
    }

    /**
     * Verifica login de usuario.
     * @param username Nombre de usuario
     * @param rawPassword Contraseña en texto plano
     * @return true si login válido
     */
    @Transactional(readOnly = true)
    public boolean login(String username, String rawPassword) {
        User user = userRepository.findByUsername(username);

        if (user == null || !user.getActive()) {
            return false;
        }

        return passwordEncoder.matches(rawPassword, user.getPassword());
    }

    /**
     * Busca usuario por username.
     * @param username Nombre de usuario
     * @return Usuario encontrado o null
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
