package com.example.anime_news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.News;

@Controller
public class IndexController {
    @Autowired
    private NewsDao newsDao;

    // 新增新闻
    @RequestMapping("/add/model")
    @ResponseBody
    public News addByModel(@ModelAttribute("news") News news) {
        return newsDao.save(news);
    }

    // 查询所有新闻
    @RequestMapping("/list")
    public ModelAndView newsList() {
        ModelAndView modelAndView = new ModelAndView("list");
        modelAndView.addObject("newsList", newsDao.findAll());
        return modelAndView;
    }

}
