package com.finalproject.Final.config;




import org.springframework.http.MediaType; // မှန်ကန်သော Spring MediaType ကို Import လုပ်ခြင်း

import java.util.HashMap;
import java.util.Map;




import org.springframework.http.MediaType; // မှန်ကန်သော Spring MediaType ကို Import လုပ်ခြင်း

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

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
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
        // URL အဆုံးက .pptx သို့မဟုတ် အခြား extension တွေကြောင့် 400 Error မတက်အောင် တားဆီးခြင်း
        configurer.favorParameter(false)
                  .ignoreAcceptHeader(false)
                  .defaultContentType(MediaType.APPLICATION_OCTET_STREAM);

        // Spring Boot 3.x / Spring 6 အတွက် Map သုံးပြီး စနစ်တကျ သတ်မှတ်ပေးခြင်း
        Map<String, MediaType> mediaTypes = new HashMap<>();
        mediaTypes.put("pptx", MediaType.valueOf("application/vnd.openxmlformats-officedocument.presentationml.presentation"));
        mediaTypes.put("ppt", MediaType.valueOf("application/vnd.ms-powerpoint"));
        mediaTypes.put("pdf", MediaType.APPLICATION_PDF);
        
        configurer.mediaTypes(mediaTypes);
    }
}   
    
