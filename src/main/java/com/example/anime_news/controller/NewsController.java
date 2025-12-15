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
    
    // 添加新闻
    @RequestMapping("/add")
    @RequiresRoles("admin")
    @ResponseBody
    public News addByModel(@ModelAttribute News news) {
        return newsService.save(news);
    }

    // 编辑新闻
    @RequestMapping("/edit")
    @RequiresRoles("admin")
    @ResponseBody
    public News editByModel(@ModelAttribute News news) {
        return newsService.save(news);
    }

    // 删除新闻
    @RequestMapping("/delete/{id}")
    @RequiresRoles("admin")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        newsService.deleteById(id);
    }

    // 通过id查询新闻信息，用于填充编辑新闻的模态框
    @RequestMapping("/edit/{id}")
    @RequiresRoles("admin")
    @ResponseBody
    public News getNewsById(@PathVariable Long id) {
        return newsService.findById(id);
    }

    // 新闻管理页面
    @RequestMapping("/news")
    @RequiresRoles("admin")
    public ModelAndView newsList() {
        ModelAndView mv = new ModelAndView("news");
        mv.addObject("newsList", newsService.findAll());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }

    // 首页
    @RequestMapping("/home")
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("newsList", newsService.findAll());
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