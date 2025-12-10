package com.example.anime_news.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.News;
<<<<<<< Updated upstream:src/main/java/com/example/anime_news/controller/IndexController.java
=======
import com.example.anime_news.pojo.User;
import com.example.anime_news.service.NewsService;
>>>>>>> Stashed changes:src/main/java/com/example/anime_news/controller/NewsController.java

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

    // 删除新闻
    @RequestMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        newsDao.deleteById(id);
    }

    // 查询所有新闻
    @RequestMapping("/list")
    public ModelAndView newsList() {
        ModelAndView modelAndView = new ModelAndView("list");
<<<<<<< Updated upstream:src/main/java/com/example/anime_news/controller/IndexController.java
        modelAndView.addObject("newsList", newsDao.findAll());
=======
        modelAndView.addObject("newsList", newsService.findAll());
        // 获取当前用户头像与用户名
        Subject subject = SecurityUtils.getSubject();
        User user = (User) subject.getPrincipal();
        modelAndView.addObject("avatar", user.getAvatar());
        modelAndView.addObject("username", user.getName());

>>>>>>> Stashed changes:src/main/java/com/example/anime_news/controller/NewsController.java
        return modelAndView;
    }
}