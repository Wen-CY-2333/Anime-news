package com.example.anime_news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.News;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    @Autowired
    private LikeService likeService;
    
    public News save(News news) {
        return newsDao.save(news);
    }

    public void deleteById(Long id) {
        newsDao.deleteById(id);
    }

    public List<News> findAll() {
        List<News> newsList = newsDao.findAll();
        for (News news : newsList){
            long likeCount = likeService.getLikeCount(news.getId());
            news.setLikeCount((int) likeCount);
        }
        return newsList;
    }
    public News findById(Long id) {
        News news = newsDao.findById(id).orElse(null);
        if (news != null) {
            long likeCount = likeService.getLikeCount(news.getId());
            news.setLikeCount((int) likeCount);
        }
        return news;
    }


    
    // 统计所有标签及其出现次数
    public Map<String, Long> countTags() {
        List<News> newsList = newsDao.findAll();
        Map<String, Long> tagCountMap = new HashMap<>();
        
        for (News news : newsList) {
            String tag = news.getTag();
            if (tag != null && !tag.isEmpty()) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0L) + 1);
            }
        }
        
        return tagCountMap;
    }
}