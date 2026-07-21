package com.finalproject.Final.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	
	
//	private RoleInterceptor roleInterceptor;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:D:/upload/");
    }
    
    //sai
    //for role validation
    //dont delete this
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//
//
//        registry.addInterceptor(roleInterceptor)
//                .addPathPatterns(
//                    "/admin/**",
//                    "/dashboard/**",
//                    "/attendance/**",
//                    "/announce/**",
//                    "/student/**",
//                    "/forgot-password"
//                );
//
//    }

}