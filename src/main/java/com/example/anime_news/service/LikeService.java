package com.example.anime_news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @CachePut(value = "like", key = "'id:' + #like.id")
    public Like save(Like like){
        return likeDao.save(like);
    }
    
    // 根据ID删除点赞
    @CacheEvict(value = "like", key = "'id:' + #id")
    public void deleteById(Long id) {
        likeDao.deleteById(id);
    }
    
    // 根据用户ID和新闻ID取消点赞
    @Transactional
    @CacheEvict(value = "like", key = "'userId:' + #userId + ':newsId:' + #newsId")
    public void deleteByUserIdAndNewsId(Long userId, Long newsId) {
        likeDao.deleteByUserIdAndNewsId(userId, newsId);
    }

    // 获取点赞状态，true表示已点赞，false表示未点赞
    @Cacheable(value = "like", key = "'status:userId:' + #userId + ':newsId:' + #newsId")
    public boolean isLiked(Long userId , Long newsId){
        return likeDao.findByUserIdAndNewsId(userId, newsId) != null;
    }

    // 获取所有点赞，支持分页
    @Cacheable(value = "like", key = "'page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<Like> findAll(Pageable pageable) {
        return likeDao.findAll(pageable);
    }
    
    // 获取所有点赞列表
    @Cacheable(value = "like", key = "'all'")
    public List<Like> findAll() {
        return likeDao.findAll();
    }

    // 获取每篇新闻的点赞数
    @Cacheable(value = "like", key = "'count:all'")
    public Map<Long, Long> countLikes() {
        List<News> newsList = newsDao.findAll();
        Map<Long, Long> likeCountMap = new HashMap<>();
        for (News news : newsList) {
            long count = likeDao.countByNewsId(news.getId());
            likeCountMap.put(news.getId(), count);
        }
        return likeCountMap;
    }
    
    // 获取单个新闻的点赞数
    @Cacheable(value = "like", key = "'count:newsId:' + #newsId")
    public Long getLikeCountByNewsId(Long newsId) {
        return likeDao.countByNewsId(newsId);
    }
}