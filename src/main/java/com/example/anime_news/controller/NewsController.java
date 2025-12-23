package com.example.anime_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;

@Controller
public class NewsController {
    @Autowired
    private NewsService newsService;

    // 新闻内容页
    @GetMapping("/news/{id}")
    public ModelAndView content(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("news");
        mv.addObject("news", newsService.findById(id));
        mv.addObject("tagCountMap", newsService.countTags());

        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("isAdmin", UserUtils.isAdmin());

        return mv;
    }

}