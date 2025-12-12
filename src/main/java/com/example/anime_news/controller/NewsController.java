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
import com.example.anime_news.pojo.User;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;


@Controller
public class NewsController {
    @Autowired
    private NewsService newsService;
    
    // 新增新闻
    @RequestMapping("/add/model")
    @ResponseBody
    public News addByModel(@ModelAttribute News news) {
        return newsService.save(news);
    }

    // 更新新闻
    @RequestMapping("/edit/model")
    @ResponseBody
    public News editByModel(@ModelAttribute News news) {
        return newsService.save(news);
    }

    // 删除新闻
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    // 编辑新闻页面
    @RequestMapping("/edit/{id}")
    @RequiresRoles("admin")
    public ModelAndView edit(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("edit");
        News news = newsService.findAll().stream()
                .filter(n -> n.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("新闻不存在"));
        mv.addObject("news", news);
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }

    // 新闻列表页面
    @RequestMapping("/list")
    @RequiresRoles("admin")
    public ModelAndView newsList() {
        ModelAndView mv = new ModelAndView("list");
        mv.addObject("newsList", newsService.findAll());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }
    
    // 新增新闻页面
    @RequestMapping("/add")
    @RequiresRoles("admin")
    public ModelAndView add() {
        ModelAndView mv = new ModelAndView("add");
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }

    // 首页
    @RequestMapping("/")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("newsList", newsService.findAll());
        
        // 检查用户是否已登录
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser != null) {
            mv.addObject("userName", currentUser.getName());
            mv.addObject("avatar", currentUser.getAvatar());
            mv.addObject("isAdmin", UserUtils.isAdmin());
            mv.addObject("isLogin", true);
        } else {
            mv.addObject("userName", "游客");
            mv.addObject("avatar", "/img/avatar.jpg");
            mv.addObject("isAdmin", false);
            mv.addObject("isLogin", false);
        }
        
        return mv;
    }
}