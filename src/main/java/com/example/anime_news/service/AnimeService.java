package com.example.anime_news.service;

import com.example.anime_news.dao.AnimeDao;
import com.example.anime_news.pojo.Anime;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
        Pageable pageable = PageRequest.of(page, size, Sort.by("date"));
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
    
    // Bilibili番剧更新API
    private static final String BANGUMI_TIMELINE_API = "https://bangumi.bilibili.com/web_api/timeline_global";

    // 更新番剧信息
    public void updateAnime() throws IOException {
        try {
            // 调用API获取数据
            Connection.Response response = Jsoup.connect(BANGUMI_TIMELINE_API)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                    .timeout(10000)
                    .ignoreContentType(true)
                    .execute();

            if (response.statusCode() != 200) {
                throw new IOException("API响应异常: " + response.statusCode());
            }

            // 解析JSON数据
            Map<String, Object> data = JsonParserFactory.getJsonParser().parseMap(response.body());
            List<Map<String, Object>> results = (List<Map<String, Object>>) data.get("result");

            if (results == null || results.isEmpty()) {
                throw new IOException("API返回数据为空");
            }

            // 清空现有数据
            animeDao.deleteAll();

            int count = 0;
            // 遍历结果
            for (Map<String, Object> result : results) {
                List<Map<String, Object>> seasons = (List<Map<String, Object>>) result.get("seasons");

                if (seasons == null || seasons.isEmpty()) {
                    continue;
                }

                // 遍历番剧列表
                for (Map<String, Object> season : seasons) {
                    try {
                        Anime anime = new Anime();
                        anime.setDate((String) result.get("date"));
                        anime.setWeek((Integer) result.get("day_of_week"));
                        anime.setIsToday((Integer) result.get("is_today"));

                        anime.setCover((String) season.get("cover"));
                        anime.setPubIndex((String) season.get("pub_index"));
                        anime.setPubTime((String) season.get("pub_time"));
                        anime.setTitle((String) season.get("title"));
                        anime.setUrl((String) season.get("url"));
                        anime.setSeasonId(((Number) season.get("season_id")).longValue());

                        // 保存
                        animeDao.save(anime);
                        count++;
                    } catch (Exception e) {
                        System.err.println("处理番剧数据时出错: " + e.getMessage());
                        continue;
                    }
                }
            }

            System.out.println("成功更新 " + count + " 条番剧数据");

        } catch (Exception e) {
            System.err.println("更新番剧数据失败: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("更新番剧数据失败", e);
        }
    }
}