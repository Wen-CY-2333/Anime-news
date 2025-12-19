package com.example.anime_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.User;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;

@Controller
public class TagController {
    @Autowired
    private NewsService newsService;

    // 标签页
    @GetMapping("/tag")
    public ModelAndView tagPage(
            @RequestParam String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size) {
        ModelAndView mv = new ModelAndView("tag");
        mv.addObject("tag", tag);
        mv.addObject("page", newsService.findByTag(tag, page, size));
        mv.addObject("newsList", newsService.findByTag(tag, page, size).getContent());
        mv.addObject("tagCountMap", newsService.countTags());
        mv.addObject("currentTag", tag);

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

        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }
}
