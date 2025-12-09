package com.example.anime_news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/** 
  * Swagger2配置类 
  **/ 
 @Configuration 
 @EnableSwagger2 
 public class Swagger2Config extends WebMvcConfigurationSupport { 
 
     /** 
      * 自定义docket 
      */ 
     @Bean 
     public Docket applicationApiConfig() { 
         return new Docket(DocumentationType.SWAGGER_2)  // 文档类型 
                 .groupName("applicationApi")    // 分组 
                 .apiInfo(applicationApiInfo())  // 调用apiInfo方法,创建一个ApiInfo实例 
                 .select()
                 .build(); 
 
     } 
 
     /** 
      * 构建并返回api文档的详细信息 
      */ 
     private ApiInfo applicationApiInfo() { 
         return new ApiInfoBuilder() 
                 .title("系统API文档接口测试")    // 标题 
                 .description("本文档描述接口测试用例")  // 描述 
                 .version("1.0")  // 版本 
                 .build(); 
     }

     /**
     * 解决swagger-ui.html 404无法访问的问题
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 解决静态资源无法访问
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
        // 解决swagger无法访问
        registry.addResourceHandler("/swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        // 解决swagger的js文件无法访问
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
 }