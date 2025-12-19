package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.News;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;


@Controller
@RequestMapping("/news")
@RequiresRoles("admin")
public class NewsController {
    @Autowired
    private NewsService newsService;
    
    // 添加新闻
    @PostMapping("/add")
    @ResponseBody
    public void addByModel(@ModelAttribute News news) {
        newsService.save(news);
    }

    // 编辑新闻
    @PostMapping("/edit")
    @ResponseBody
    public void editByModel(@ModelAttribute News news) {
        newsService.save(news);
    }

    // 删除新闻
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    // 通过id查询新闻信息
    @GetMapping("/find/{id}")
    @ResponseBody
    public News getNewsById(@PathVariable Long id) {
        return newsService.findById(id);
    }

    // 新闻管理页面
    @GetMapping("/")
    public ModelAndView newsList(@RequestParam(defaultValue = "0") int page, 
                                 @RequestParam(defaultValue = "10") int size) {
        ModelAndView mv = new ModelAndView("news");
        mv.addObject("page", newsService.findAll(page, size));
        mv.addObject("newsList", newsService.findAll(page, size).getContent());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }

}