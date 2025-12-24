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

import com.example.anime_news.service.CrawlerService;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/crawler")
@RequiresRoles("admin")
public class CrawlerController {
    @Autowired
    private CrawlerService crawlerService;
    @Autowired
    private NewsService newsService;
    
    // 爬取新闻并保存到数据库
    @PostMapping(value = "/crawl", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String crawlNews(@RequestParam int startPage,
                            @RequestParam int endPage) {
        try {
            int[] newsCount = crawlerService.crawlNews(startPage, endPage);
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
    
    // 更新番剧信息
    @PostMapping(value = "/update-anime", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String updateAnimeInfo() {
        try {
            crawlerService.updateAnime();
            return "成功更新番剧信息";
        } catch (Exception e) {
            e.printStackTrace();
            return "更新番剧信息失败: " + e.getMessage();
        }
    }
    
    // 更新音乐信息
    @PostMapping(value = "/update-music", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String updateMusicInfo(@RequestParam String timeFrom,
                                  @RequestParam String timeTo) {
        try {
            crawlerService.updateMusic(timeFrom, timeTo);
            return "成功更新音乐信息";
        } catch (Exception e) {
            e.printStackTrace();
            return "更新音乐信息失败: " + e.getMessage();
        }
    }
    
    // 爬虫页面
    @GetMapping("/")
    public ModelAndView crawlerPage() {
        ModelAndView mv = new ModelAndView("crawler");
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        return mv;
    }
}
