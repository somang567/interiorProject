package com.keduit.interiors.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

  //Conffig에서 받아온 것
  //url이 읽어올 경우
  // addResourceLocations : 로컬 컴퓨터에서 읽어올 root 경로를 설정
  //uploadPath : 실제 파일의 경로
  @Value("${uploadPath}")
  String uploadPath;
  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/images/**")   //접근 경로 여기로 오면 (테이블 상)
        .addResourceLocations(uploadPath);  // 실제파일경로
  }
}
