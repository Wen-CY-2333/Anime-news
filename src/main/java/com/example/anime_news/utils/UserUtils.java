package com.example.anime_news.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.example.anime_news.pojo.User;

public class UserUtils {
    
    //获取当前登录用户
    public static User getCurrentUser() {
        Subject subject = SecurityUtils.getSubject();
        return (User) subject.getPrincipal();
    }
}