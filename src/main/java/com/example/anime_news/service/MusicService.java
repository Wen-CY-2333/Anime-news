package com.example.anime_news.service;

import com.example.anime_news.dao.MusicDao;
import com.example.anime_news.pojo.Music;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MusicService {

    @Autowired
    private MusicDao musicDao;

    public List<Music> findAll() {
        return musicDao.findAll();
    }

    public Music save(Music music) {
        return musicDao.save(music);
    }

    public void deleteById(Long id) {
        musicDao.deleteById(id);
    }

    public Music findById(Long id) {
        return musicDao.findById(id).orElse(null);
    }

    public Page<Music> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("play").descending());
        return musicDao.findAll(pageable);
    }

    // 根据分区ID分页查询（支持查询所有）
    public Page<Music> findByCateId(Integer cateId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("play").descending());
        if (cateId == null) {
            // 查询所有音乐
            return musicDao.findAll(pageable);
        }
        return musicDao.findByCateId(cateId, pageable);
    }
}