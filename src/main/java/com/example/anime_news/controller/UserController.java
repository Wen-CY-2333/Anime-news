package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @PostMapping(path = "/add")
    @ResponseBody
    public User addByModel(@ModelAttribute User user) {
        return userService.save(user);
    }

    // 修改用户信息
    @PostMapping(path = "/edit")
    @ResponseBody
    public User saveEditUser(@ModelAttribute User user) {
        return userService.save(user);
    }

    // 删除用户
    @PostMapping(path = "/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        userService.deleteById(id);
    }

    // 通过id查询用户信息，用于填充编辑用户的模态框
    @GetMapping(path = "/find/{id}")
    @ResponseBody
    public User editByModel(@PathVariable Long id) {
        return userService.findById(id);
    }

    // 用户管理页面
    @GetMapping(path = "/")
    public ModelAndView list(Model model, @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("users");
        mv.addObject("page", userService.findAll(page, size));
        mv.addObject("userList", userService.findAll(page, size).getContent());
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }
}