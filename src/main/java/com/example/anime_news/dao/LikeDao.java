package com.example.anime_news.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.anime_news.pojo.Like;

@Repository
public interface LikeDao extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {
    // 通过用户ID和新闻ID查找点赞记录
    Like findByUserIdAndNewsId(Long userId, Long newsId);

    // 根据用户ID和新闻ID删除点赞记录
    void deleteByUserIdAndNewsId(Long userId, Long newsId);
    // 通过新闻ID查找点赞数量
    Long countByNewsId(Long NewsId);
    
    // 检查用户是否已点赞
    boolean existsByUserIdAndNewsId(Long userId, Long newsId);
}