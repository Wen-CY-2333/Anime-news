package com.example.anime_news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.News;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public News save(News news) {
        return newsDao.save(news);
    }

    public void deleteById(Long id) {
        newsDao.deleteById(id);
    }
    
    public void deleteAll() {
        newsDao.deleteAll();
    }

    public News findByTitle(String title) {
        return newsDao.findByTitle(title);
    }

    public News findById(Long id) {
        return newsDao.findById(id).orElse(null);
    }

    public List<News> findAll() {
        return newsDao.findAll();
    }
    
    // 分页查询所有新闻
    public Page<News> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, org.springframework.data.domain.Sort.by("time").descending());
        return newsDao.findAll(pageable);
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