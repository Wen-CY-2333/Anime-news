package com.example.anime_news.service;

import com.example.anime_news.dao.AnimeDao;
import com.example.anime_news.pojo.Anime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnimeService {

    @Autowired
    private AnimeDao animeDao;

    public List<Anime> findAll() {
        return animeDao.findAll();
    }

    public Anime save(Anime anime) {
        return animeDao.save(anime);
    }

    public void deleteById(Long id) {
        animeDao.deleteById(id);
    }

    public Anime findById(Long id) {
        return animeDao.findById(id).orElse(null);
    }

    public Page<Anime> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return animeDao.findAll(pageable);
    }

    //根据星期分页查询（支持查询所有）
    public Page<Anime> findByWeek(Integer week, int page, int size) {
        if (week == null) {
            // 查询所有番剧
            return animeDao.findAll(PageRequest.of(page, size));
        }
        return animeDao.findByWeek(week, PageRequest.of(page, size));
    }

    //查询今日更新（支持分页）
    public Page<Anime> findTodayAnime(int page, int size) {
        return animeDao.findByIsToday(1, PageRequest.of(page, size));
    }
}