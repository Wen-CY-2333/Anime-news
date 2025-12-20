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
public class HomeController {
    @Autowired
    private NewsService newsService;

    // logout默认跳转到/， 找不到修改的方法，所以手动重定向到/home
    @GetMapping("/") 
    public String redirectToHome() {
        return "redirect:/home";
    }

    // 首页
    @GetMapping({ "/home" })
    public ModelAndView home(@RequestParam(defaultValue = "0") int page,
                             @RequestParam(defaultValue = "8") int size) {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("newsList", newsService.findAll(page, size).getContent());
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

        mv.addObject("page", newsService.findAll(page, size));
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }

}
