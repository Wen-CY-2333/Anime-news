package com.example.anime_news.dao;

import com.example.anime_news.pojo.Music;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicDao extends JpaRepository<Music, Long> {
    // 根据视频ID查询
    Music findByBvid(String bvid);

    // 根据视频ID删除
    void deleteByBvid(String bvid);

    // 根据分区ID分页查询
    Page<Music> findByCateId(Integer cateId, Pageable pageable);
}