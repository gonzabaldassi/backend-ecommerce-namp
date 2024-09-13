package com.namp.ecommerce;

import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // Configuracion del acceso a la carpeta de imagenes
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/images/");
    }
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/*")
                .allowedOrigins("http://localhost:3000") // Dirección del frontend
                .allowedMethods("");
    }
}
