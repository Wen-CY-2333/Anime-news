package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.service.NewsService;
import com.example.anime_news.service.SpiderService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/spider")
@RequiresRoles("admin")
public class SpiderController {
    @Autowired
    private SpiderService spiderService;
    @Autowired
    private NewsService newsService;
    
    // 爬虫页面
    @GetMapping("/")
    public ModelAndView spiderPage() {
        ModelAndView mv = new ModelAndView("spider");
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }
    
    // 爬取新闻并保存到数据库
    @PostMapping(value = "/crawl", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String crawlNews(@RequestParam int startPage,
                            @RequestParam int endPage) {
        try {
            int[] newsCount = spiderService.crawlNews(startPage, endPage);
            return "成功爬取并保存 " + newsCount[0] + " 条新闻，" + newsCount[1] + " 条新闻已存在";
        } catch (Exception e) {
            e.printStackTrace();
            return "爬取失败: " + e.getMessage();
        }
    }
    
    // 清空数据库中的新闻
    @PostMapping(value = "/clear", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String clearNews() {
        try {
            newsService.deleteAll();
            return "数据库中的新闻已清空";
        } catch (Exception e) {
            e.printStackTrace();
            return "清空数据库失败: " + e.getMessage();
        }
    }
}
