package com.keduit.interiors.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ProductWebMvcConfig implements WebMvcConfigurer {

	@Value("${itemImgLocation}")
	String itemImgLocation;
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/item/**")
				.addResourceLocations("file://" + itemImgLocation + "/");
	}
}
