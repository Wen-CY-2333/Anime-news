package com.example.anime_news.service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.News;

@Service
public class TaskService {
    @Autowired
    private NewsDao newsDao;

    @Autowired
    private CrawlerService crawlerService;

    // 定时任务：每1分钟自动增加一次新闻访问量
    @Scheduled(cron = "0 */1 * * * ?")
    public void autoAddNewsVisits() {
        List<News> newsList = newsDao.findAll();
        Random random = new Random();
        for (News news : newsList) {
            Integer old = news.getVisits() == null ? 0 : news.getVisits();
            news.setVisits(old + random.nextInt(10));
            newsDao.save(news);
        }
    }

    // 定时任务：每天自动爬取新闻
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoCrawlNews() {
        try {
            crawlerService.crawlNews(1, 3);
        } catch (IOException e) {
            System.out.println("爬取新闻信息失败");
            e.printStackTrace();
        }
    }

    // 每天自动更新番剧
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoUpdateAnime() {
        try {
            crawlerService.updateAnime();
        } catch (IOException e) {
            System.out.println("更新番剧信息失败");
            e.printStackTrace();
        }
    }

    // 每天自动更新音乐
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoUpdateMusic() {
        //获取当前日期
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        //计算当月第一天
        String firstDayOfMonth = currentDate.substring(0, 6) + "01";

        try {
            crawlerService.updateMusic(firstDayOfMonth, currentDate);
        } catch (IOException e) {
            System.out.println("更新音乐信息失败");
            e.printStackTrace();
        }
    }
}
