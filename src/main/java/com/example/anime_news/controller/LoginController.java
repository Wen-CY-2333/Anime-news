package com.example.anime_news.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import com.example.anime_news.pojo.User;
import com.example.anime_news.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //登录
    @PostMapping("/dologin")
    public String dologin(HttpServletRequest request, String username, String password, RedirectAttributes redirectAttributes) {
        try {
            SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password));
            // 使用Shiro内置的SavedRequest功能跳转回之前请求的页面
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if (savedRequest != null) {
                String requestUrl = savedRequest.getRequestUrl();
                return "redirect:" + requestUrl;
            }
            // 默认跳转首页
            return "redirect:/home";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("loginError", "用户名或密码错误");
            return "redirect:/login";
        }
    }
    
    //注册
    @PostMapping("/doregister")
    public String doregister(HttpServletRequest request, String username, String password, String avatar, RedirectAttributes redirectAttributes) {

        //检查用户名是否已存在
        User existingUser = userService.findTopByName(username);
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("registerError", "用户名已存在");
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
            // 使用Shiro内置的SavedRequest功能跳转回之前请求的页面
            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            if (savedRequest != null) {
                String requestUrl = savedRequest.getRequestUrl();
                return "redirect:" + requestUrl;
            }
            // 默认跳转首页
            return "redirect:/home";
        } catch (Exception e) {
            return "redirect:/login";
        }
    }

}

