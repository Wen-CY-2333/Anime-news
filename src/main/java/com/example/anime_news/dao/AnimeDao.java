package com.example.anime_news.dao;

import com.example.anime_news.pojo.Anime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeDao extends JpaRepository<Anime, Long> {
    // 根据番剧ID查询
    Anime findBySeasonId(Long seasonId);

    //根据番剧ID删除
    void deleteBySeasonId(Long seasonId);

    //根据星期分页查询
    Page<Anime> findByWeek(Integer week, Pageable pageable);

    //查询今日更新
    Page<Anime> findByIsToday(Integer isToday, Pageable pageable);

}