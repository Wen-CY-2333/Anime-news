package com.example.anime_news.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.anime_news.pojo.User;
import com.example.anime_news.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    //登录
    @RequestMapping("/dologin")
    public String dologin(String username, String password) {
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
            return "redirect:/";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }
    
    //注册
    @RequestMapping("/doregister")
    public String doregister(String username, String password, String avatar) {

        //检查用户名是否已存在
        User existingUser = userService.findTopByName(username);
        if (existingUser != null) {
            return "redirect:/login";
        }
        
        // 创建新用户
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        user.setAvatar(avatar);
        user.setRole("user");
        userService.save(user);
        
        // 注册成功后自动登录
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
            return "redirect:/list";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

    //登出
    @RequestMapping("/logout")
    public String logout() {
        SecurityUtils.getSubject().logout();
        return "redirect:/login";
    }

}

