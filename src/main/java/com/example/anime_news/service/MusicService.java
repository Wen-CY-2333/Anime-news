package com.example.anime_news.service;

import com.example.anime_news.dao.MusicDao;
import com.example.anime_news.pojo.Music;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return musicDao.findAll(PageRequest.of(page, size));
    }

    // 根据分区ID分页查询（支持查询所有）
    public Page<Music> findByCateId(Integer cateId, int page, int size) {
        if (cateId == null) {
            // 查询所有音乐
            return musicDao.findAll(PageRequest.of(page, size));
        }
        return musicDao.findByCateId(cateId, PageRequest.of(page, size));
    }
    
    // 音乐分区映射
    private static final Map<Integer, String> CATE_NAME_MAP = new HashMap<>();
    static {
        CATE_NAME_MAP.put(28, "原创音乐");
        CATE_NAME_MAP.put(31, "翻唱");
        CATE_NAME_MAP.put(30, "VOCALOID·UTAU");
        CATE_NAME_MAP.put(194, "电音");
        CATE_NAME_MAP.put(59, "演奏");
        CATE_NAME_MAP.put(193, "MV");
        CATE_NAME_MAP.put(29, "音乐现场");
        CATE_NAME_MAP.put(130, "音乐综合");
    }

    // Bilibili音乐区热门视频API
    private static final String MUSIC_HOT_API = "https://s.search.bilibili.com/cate/search?main_ver=v3&search_type=video&view_type=hot_rank&order=click&page=1&pagesize=50&jsonp=jsonp&";

    // 更新音乐信息
    public void updateMusic(String timeFrom, String timeTo) throws IOException {
        try {
            // 检查时间参数是否为空
            if (timeFrom == null || timeTo == null || timeFrom.isEmpty() || timeTo.isEmpty()) {
                throw new IllegalArgumentException("时间参数不能为空");
            }

            // 清空现有数据
            musicDao.deleteAll();
            int totalCount = 0;

            // 遍历所有音乐分区
            for (Map.Entry<Integer, String> entry : CATE_NAME_MAP.entrySet()) {
                Integer cateId = entry.getKey();
                String cateName = entry.getValue();

                try {
                    // 调用API获取数据
                    String apiUrl = MUSIC_HOT_API + "time_from=" + timeFrom + "&time_to=" + timeTo + "&cate_id=" + cateId + "&page=1&pagesize=50";
                    Connection.Response response = Jsoup.connect(apiUrl)
                            .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                            .timeout(10000)
                            .ignoreContentType(true)
                            .execute();

                    if (response.statusCode() != 200) {
                        System.err.println("API响应异常: " + response.statusCode() + " for cateId: " + cateId);
                        continue;
                    }

                    // 解析JSON数据
                    System.out.println("API响应内容 (cateId " + cateId + "): " + response.body().substring(0, Math.min(200, response.body().length())) + "...");
                    Map<String, Object> data = JsonParserFactory.getJsonParser().parseMap(response.body());
                    
                    // 检查API响应状态
                    Integer code = (Integer) data.get("code");
                    if (code != null && code != 0) {
                        System.err.println("API响应错误: " + data.get("msg") + " for cateId: " + cateId);
                        continue;
                    }
                    
                    // 获取结果数量信息
                    Integer numResults = (Integer) data.get("numResults");
                    System.out.println("分类 " + cateName + " (ID: " + cateId + ") 返回结果数: " + numResults);
                    
                    // 获取结果列表 - API直接返回result数组
                    List<Map<String, Object>> results = null;
                    if (data.containsKey("result")) {
                        Object resultObj = data.get("result");
                        if (resultObj instanceof List) {
                            results = (List<Map<String, Object>>) resultObj;
                        }
                    }
                    
                    if (results == null || results.isEmpty()) {
                        System.out.println("分类 " + cateName + " (ID: " + cateId + ") 在指定时间范围内没有数据，跳过");
                        continue;
                    }

                    int count = 0;
                    // 遍历结果
                    for (Map<String, Object> item : results) {
                        try {
                            Music music = new Music();
                            
                            // 处理bvid，确保是字符串类型
                            Object bvidObj = item.get("bvid");
                            if (bvidObj == null) {
                                System.err.println("数据中缺少bvid字段，跳过该记录");
                                continue;
                            }
                            String bvidStr = bvidObj.toString();
                            
                            // 处理数值字段，API返回的是字符串格式，需要转换为整数
                            Integer play = 0;
                            Integer review = 0;
                            Integer favorites = 0;
                            
                            try {
                                Object playObj = item.get("play");
                                if (playObj != null) {
                                    play = Integer.parseInt(playObj.toString());
                                }
                                
                                Object reviewObj = item.get("review");
                                if (reviewObj != null) {
                                    review = Integer.parseInt(reviewObj.toString());
                                }
                                
                                Object favObj = item.get("favorites");
                                if (favObj != null) {
                                    favorites = Integer.parseInt(favObj.toString());
                                }
                            } catch (NumberFormatException e) {
                                System.err.println("数值转换错误: " + e.getMessage());
                            }
                            
                            music.setBvid(bvidStr);
                            music.setPic((String) item.get("pic"));
                            music.setTitle((String) item.get("title"));
                            music.setUrl("https://www.bilibili.com/video/" + bvidStr);
                            music.setAuthor((String) item.get("author"));
                            music.setPlay(play);
                            music.setReview(review);
                            music.setFavorites(favorites);
                            music.setCateId(cateId);
                            music.setCateName(cateName);

                            // 保存
                            musicDao.save(music);
                            count++;
                            totalCount++;
                        } catch (Exception e) {
                            System.err.println("处理音乐数据时出错: " + e.getMessage());
                            e.printStackTrace();
                            continue;
                        }
                    }

                    System.out.println("成功更新 " + count + " 条 " + cateName + " 数据");

                } catch (Exception e) {
                    System.err.println("更新音乐分区数据失败 for cateId: " + cateId + ": " + e.getMessage());
                    continue;
                }
            }

            System.out.println("成功更新音乐数据，总计 " + totalCount + " 条");

        } catch (Exception e) {
            System.err.println("更新音乐数据失败: " + e.getMessage());
            e.printStackTrace();
            throw new IOException("更新音乐数据失败", e);
        }
    }
}