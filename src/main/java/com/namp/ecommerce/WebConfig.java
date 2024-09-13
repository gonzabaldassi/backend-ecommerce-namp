package com.namp.ecommerce;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry){
        // Configuracion del acceso a la carpeta de imagenes
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/images/");
    }
}
// Esta clase sirve para manejar recursos estaticos en carpetas personalizadas, ya que SpringBoot por defecto maneja public o static.