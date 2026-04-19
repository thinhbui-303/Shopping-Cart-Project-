package com.ecom.shopping_cart.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
class FileStorageConfig  implements WebMvcConfigurer{

    @Value("${app.upload.path}")
    private String uploadPath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry)
    {
        Path path = Paths.get(uploadPath);
        String uploadAbsPath = path.toFile().getAbsolutePath();
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/static/img/","file:" + uploadAbsPath + "/");
    }
    
}