package com.example.anime_news.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.anime_news.pojo.Like;

@Repository
public interface LikeDao extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {

}