package com.franciscoleon.blog.blog_francisco_leon.model.dto.dtoparapruebas;

/**
 *
 * DTO para recuperar la contraseña de un usuario por su ID
 * usado para pruebas en {@link com.franciscoleon.blog.blog_francisco_leon.controller.TestController TestController}
 */
public class UserPasswordDTO {
    
     private Long id;
     private String password;

     public UserPasswordDTO(Long id, String password) {
         this.id = id;
         this.password = password;
     }

     public Long getId() {
         return id;
     }

     public String getPassword() {
         return password;
     }
    
     

}
