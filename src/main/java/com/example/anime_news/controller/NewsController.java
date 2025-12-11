package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.News;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;


@Controller
public class NewsController {
    @Autowired
    private NewsService newsService;
    
    // 新增新闻
    @RequestMapping("/add/model")
    @ResponseBody
    public News addByModel(@ModelAttribute("news") News news) {
        return newsService.save(news);
    }

    // 删除新闻
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    // 查询所有新闻并获取当前用户名和头像
    @RequestMapping("/list")
    @RequiresRoles("admin")
    public ModelAndView newsList() {
        ModelAndView mv = new ModelAndView("list");
        mv.addObject("newsList", newsService.findAll());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }
}