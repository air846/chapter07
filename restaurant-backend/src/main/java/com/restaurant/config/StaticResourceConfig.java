package com.restaurant.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

/**
 * 静态资源配置
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 配置虚拟路径映射到上传文件的实际路径
        String uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize().toString();
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath + "/");

        // 确保静态资源可以访问
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");

        // 兼容数据库旧数据中只存文件名的情况，直接映射根路径图片到 classpath:/static/images/
        registry.addResourceHandler("/*.jpg", "/*.png", "/*.jpeg")
                .addResourceLocations("classpath:/static/images/");
    }
}
