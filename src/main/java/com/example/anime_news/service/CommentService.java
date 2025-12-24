package com.example.anime_news.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.CommentDao;
import com.example.anime_news.pojo.Comment;

@Service
public class CommentService {
    @Autowired
    private CommentDao commentDao;

    public Comment save(Comment comment) {
        if (comment.getCommentContent() == null || comment.getCommentContent().trim().isEmpty()) {
            throw new IllegalArgumentException("评论内容不能为空");
        }
        if (comment.getCreateTime() == null) {
            comment.setCreateTime(new Date());
        }
        return commentDao.save(comment);
    }

    public void deleteById(Long id) {
        commentDao.deleteById(id);
    }

    public List<Comment> findAll() {
        return commentDao.findAll();
    }
    
    public List<Comment> findByNewsId(Long newsId) {
        return commentDao.findByNewsIdOrderByCreateTimeDesc(newsId);
    }
    
    public long countByNewsId(Long newsId) {
        return commentDao.countByNewsId(newsId);
    }
}