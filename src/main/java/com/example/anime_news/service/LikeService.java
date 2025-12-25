package com.example.anime_news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.anime_news.dao.LikeDao;
import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.Like;
import com.example.anime_news.pojo.News;

@Service
public class LikeService {
    
    @Autowired
    private LikeDao likeDao;
    
    @Autowired
    private NewsDao newsDao;

    // 点赞
    public Like save(Like like){
        return likeDao.save(like);
    }
    
    // 根据用户ID和新闻ID取消点赞
    @Transactional
    public void deleteByUserIdAndNewsId(Long userId, Long newsId) {
        likeDao.deleteByUserIdAndNewsId(userId, newsId);
    }

    // 获取点赞状态，true表示已点赞，false表示未点赞
    public boolean isLiked(Long userId , Long newsId){
        return likeDao.findByUserIdAndNewsId(userId, newsId) != null;
    }

    // 获取每篇新闻的点赞数
    public Map<Long, Long> countLikes() {
        List<News> newsList = newsDao.findAll();
        Map<Long, Long> likeCountMap = new HashMap<>();
        for (News news : newsList) {
            long count = likeDao.countByNewsId(news.getId());
            likeCountMap.put(news.getId(), count);
        }
        return likeCountMap;
    }
}