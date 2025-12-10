package com.example.anime_news.config;

import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.anime_news.dao.UserDao;

@Configuration
public class ShiroConfig {

    @Bean
    public org.apache.shiro.realm.Realm realm(UserDao userDao) {
        return new Realm(userDao);
    }

    @Bean
    public ShiroFilterChainDefinition filter() { 
        DefaultShiroFilterChainDefinition cd = new DefaultShiroFilterChainDefinition();
        cd.addPathDefinition("/login", "anon");
        cd.addPathDefinition("/dologin", "anon");
        cd.addPathDefinition("/doregister", "anon");
        cd.addPathDefinition("/h2/**", "anon");
        cd.addPathDefinition("/logout", "logout");
        cd.addPathDefinition("/**", "authc");
        return cd;
    }
}
