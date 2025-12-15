package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.User;
import com.example.anime_news.service.UserService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/user")
@RequiresRoles("admin")
public class UserController {
    @Autowired
    private UserService userService;

    // 添加用户
    @RequestMapping(path="/add")
    @ResponseBody
    public User addByModel(@ModelAttribute User user) {
        return userService.save(user);
    }
    
    // 修改用户信息
    @RequestMapping(path="/edit")
    @ResponseBody
    public User saveEditUser(@ModelAttribute User user) {
        return userService.save(user);
    }

    // 删除用户
    @RequestMapping(path="/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }
    
    // 通过id查询用户信息，用于填充编辑用户的模态框
    @RequestMapping(path="/edit/{id}")
    @ResponseBody
    public User editByModel(@PathVariable Long id) {
        return userService.findById(id);
    }

    // 用户管理页面
    @RequestMapping(path="/list")
    public ModelAndView list(Model model) {
        ModelAndView mv = new ModelAndView("users");
        mv.addObject("userList", userService.findAll());
        // 检查用户是否已登录
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser != null) {
            mv.addObject("userName", currentUser.getName());
            mv.addObject("avatar", currentUser.getAvatar());
            mv.addObject("isAdmin", UserUtils.isAdmin());
            mv.addObject("isLogin", true);
        } else {
            mv.addObject("userName", "游客");
            mv.addObject("avatar", "/img/index.jpg");
            mv.addObject("isAdmin", false);
            mv.addObject("isLogin", false);
        }
        return mv;
    }
}