package com.example.anime_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.User;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;

@Controller
public class ContentController {
    @Autowired
    private NewsService newsService;

    // 新闻内容页
    @GetMapping("/news/content/{id}")
    public ModelAndView content(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("content");
        mv.addObject("news", newsService.findById(id));
        mv.addObject("tagCountMap", newsService.countTags());

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