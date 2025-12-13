package com.example.anime_news.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.LikeDao;
import com.example.anime_news.pojo.Like;

@Service
public class LikeService {
    @Autowired
    private LikeDao likeDao;

    // 点赞或者删除点赞
    // true表示点赞成功，false表示取消点赞
    public boolean like(Long userId , Long newsId){
        Like like = likeDao.findByUserIdAndNewsId(userId, newsId);
        if(like == null){
            // 添加点赞数据
            Like newLike = new Like();
            newLike.setUserId(userId);
            newLike.setNewsId(newsId);
            likeDao.save(newLike);
            return true;
        }else{
            likeDao.deleteByUserIdAndNewsId(userId, newsId);
            return false;
        }
    }

    // 获取点赞数量
    public Long getLikeCount(Long newsId){
        return likeDao.countByNewsId(newsId);
    }


    // 检查用户是否点赞

    public boolean isLiked(Long userId, Long newsId){
        //表示我在新闻点赞中是否找到点赞数据
        return likeDao.findByUserIdAndNewsId(userId,newsId) != null;
    }

}