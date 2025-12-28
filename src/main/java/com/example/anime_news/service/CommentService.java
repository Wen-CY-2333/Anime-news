package com.example.anime_news.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.CommentDao;
import com.example.anime_news.pojo.Comment;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    @CachePut(value = "comment", key = "'id:' + #comment.id")
    @Caching(evict = {
        @CacheEvict(value = "news", allEntries = true),
        @CacheEvict(value = "comment", key = "'newsId:' + #comment.newsId"),
        @CacheEvict(value = "comment", key = "'count:newsId:' + #comment.newsId"),
        @CacheEvict(value = "comment", key = "'all'"),
        @CacheEvict(value = "news", key = "'count:comments'")
    })
    public Comment save(Comment comment) {
        if (comment.getCommentContent() == null || comment.getCommentContent().trim().isEmpty()) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        if (comment.getCreateTime() == null) {
            comment.setCreateTime(new Date());
        }
        return commentDao.save(comment);
    }

    @Caching(evict = {
        @CacheEvict(value = "comment", allEntries = true), // 清除所有评论缓存
        @CacheEvict(value = "news", allEntries = true), // 清除所有新闻缓存
        @CacheEvict(value = "news", key = "'count:comments'") // 清除新闻评论数量映射缓存
    })
    public void deleteById(Long id) {
        commentDao.deleteById(id);
    }

    @Cacheable(value = "comment", key = "'all'")
    public List<Comment> findAll() {
        return commentDao.findAll();
    }
    
    @Cacheable(value = "comment", key = "'page:' + #pageable.pageNumber + ':size:' + #pageable.pageSize")
    public Page<Comment> findAll(Pageable pageable) {
        return commentDao.findAll(pageable);
    }
    
    @Cacheable(value = "comment", key = "'newsId:' + #newsId")
    public List<Comment> findByNewsId(Long newsId) {
        return commentDao.findByNewsIdOrderByCreateTimeDesc(newsId);
    }
    
    @Cacheable(value = "comment", key = "'count:newsId:' + #newsId")
    public long countByNewsId(Long newsId) {
        return commentDao.countByNewsId(newsId);
    }
}