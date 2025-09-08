package com.franciscoleon.blog.blog_francisco_leon.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {
    
    private Jwt jwt = new Jwt();
    private Frontend frontend = new Frontend();
    
    public Jwt getJwt() {
        return jwt;
    }
    
    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }
    
    public Frontend getFrontend() {
        return frontend;
    }
    
    public void setFrontend(Frontend frontend) {
        this.frontend = frontend;
    }
    
    public static class Jwt {
        private String secret;
        
        public String getSecret() {
            return secret;
        }
        
        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
    
    public static class Frontend {
        private String url;
        
        public String getUrl() {
            return url;
        }
        
        public void setUrl(String url) {
            this.url = url;
        }
    }
}