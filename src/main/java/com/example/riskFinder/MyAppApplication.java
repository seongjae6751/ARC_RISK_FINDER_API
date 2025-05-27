package com.example.riskFinder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.util.unit.DataSize;

import jakarta.servlet.MultipartConfigElement;

@SpringBootApplication
public class MyAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyAppApplication.class, args);
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(20));     // 파일 하나당 최대 크기
        factory.setMaxRequestSize(DataSize.ofMegabytes(20));  // 전체 요청 최대 크기
        return factory.createMultipartConfig();
    }
}
