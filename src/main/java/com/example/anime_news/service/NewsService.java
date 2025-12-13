package com.example.anime_news.service;

import java.util.List;

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


}