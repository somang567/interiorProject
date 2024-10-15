package com.keduit.interiors.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}") // 설정 파일에서 업로드 경로를 주입
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 운영체제에 따라 경로 구분자를 조정
        String path = System.getProperty("user.dir") + "/" + uploadDir + "/";

        registry.addResourceHandler("/files/**")
                .addResourceLocations("file:///" + path);
    }
}
