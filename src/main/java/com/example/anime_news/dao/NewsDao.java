package com.example.anime_news.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.anime_news.pojo.News;

/**
 * 创建Dao层类
 * 1.使用@Repository，标识为Dao层类
 * 2.继承JpaRepository接口，指定实体类型和主键类型，扩展接口方法
 */
@Repository
public interface NewsDao extends JpaRepository<News, Long>, JpaSpecificationExecutor<News> {
    
    News findByTitle(String title);
}
