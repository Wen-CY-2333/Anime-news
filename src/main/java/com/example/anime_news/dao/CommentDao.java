package com.example.anime_news.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.anime_news.pojo.Comment;

@Repository
public interface CommentDao extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {
    
    List<Comment> findByNewsIdOrderByCreateTimeDesc(Long newsId);
    
    long countByNewsId(Long newsId);
}