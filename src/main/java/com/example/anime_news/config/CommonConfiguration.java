package com.example.anime_news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * 创建配置类，负责配置跨域处理（默认情况下，url无法被跨域访问）
 * 1.使用@Configuration，标注为配置类
 * 2.使用@Bean，标注方法返回为组件
 * 3.创建CorsFilter对象并返回
 * （问题：CorsFilter没有看到明显的被调用痕迹，它是如何生效运行的呢？） 
 * （答案：CorsFilter是过滤器接口的实现类，会被spring boot web在启动时自动扫描，每次接收到url请求时会做相应处理）
 */
@Configuration
public class CommonConfiguration {

    @Bean
    public CorsFilter corsFilter() {
        // 创建跨域配置
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("*");// 允许任何域名
        corsConfiguration.addAllowedHeader("*");// 允许任何头
        corsConfiguration.addAllowedMethod("*");// 允许任何方法
        // 注册到过滤器中
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);// 全部url都支持跨域访问
        return new CorsFilter(source);
    }
}
