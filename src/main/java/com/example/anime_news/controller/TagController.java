package com.example.anime_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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
        mv.addObject("newsList", newsService.findByTag(tag, page, size).getContent());
        mv.addObject("tagCountMap", newsService.countTags());
        mv.addObject("currentTag", tag);

        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("isAdmin", UserUtils.isAdmin());

        mv.addObject("page", newsService.findByTag(tag, page, size));
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }
}
