package com.restaurant.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.File;

/**
 * 文件存储配置
 */
@Configuration
public class FileStorageConfig {

    @Value("${file.upload-dir}")
    private String uploadDir;

    /**
     * 初始化文件存储目录
     */
    @PostConstruct
    public void init() {
        try {
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
                System.out.println("创建上传目录: " + directory.getAbsolutePath());
            }

            // 创建静态资源目录作为符号链接
            File imagesDir = new File("./src/main/resources/static/images");
            if (!imagesDir.exists()) {
                imagesDir.mkdirs();
                System.out.println("创建静态图片目录: " + imagesDir.getAbsolutePath());
            }
        } catch (Exception e) {
            throw new RuntimeException("无法初始化文件存储目录", e);
        }
    }
}
