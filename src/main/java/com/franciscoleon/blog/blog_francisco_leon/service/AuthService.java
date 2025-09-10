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
     * 
     * @param username    Nombre de usuario
     * @param email       Correo electrónico
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
     *
     * @param email       Correo electrónico
     * @param rawPassword Contraseña en texto plano
     * @return Usuario autenticado o null
     */
    @Transactional(readOnly = true)
    public User login(String email, String rawPassword) {
        User user = userRepository.findByEmail(email);

        if (user == null || !user.getActive() || !passwordEncoder.matches(rawPassword, user.getPassword())) {
            return null; // login fallido
        }

        return user; // login exitoso
    }

    /**
     * Busca usuario por username.
     * 
     * @param username Nombre de usuario
     * @return Usuario encontrado o null
     */
    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
