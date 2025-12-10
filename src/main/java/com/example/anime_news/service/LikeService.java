package com.example.anime_news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.LikeDao;
import com.example.anime_news.pojo.Like;

@Service
public class LikeService {
    @Autowired
    private LikeDao likeDao;

    public Like save(Like like) {
        return likeDao.save(like);
    }

    public void deleteById(Long id) {
        likeDao.deleteById(id);
    }

}