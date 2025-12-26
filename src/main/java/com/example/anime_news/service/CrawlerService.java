package com.example.anime_news.service;

import com.example.anime_news.dao.AnimeDao;
import com.example.anime_news.dao.MusicDao;
import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.Anime;
import com.example.anime_news.pojo.Music;
import com.example.anime_news.pojo.News;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CrawlerService {

    @Autowired
    private AnimeDao animeDao;

    @Autowired
    private MusicDao musicDao;

    @Autowired
    private NewsDao newsDao;

    // Bilibili番剧更新API
    private static final String BANGUMI_TIMELINE_API = "https://bangumi.bilibili.com/web_api/timeline_global";

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

    // 梦域动漫地址
    public static final String BASE_URL = "https://www.moelove.cn/";

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

    // 爬取新闻
    public int[] crawlNews(int startPage, int endPage) throws IOException {
        int[] newsCount = new int[2];
        newsCount[0] = 0;
        newsCount[1] = 0;
        for (int i = startPage; i <= endPage; i++) {
            int[] count = crawlSinglePage(i);
            System.out.println("第 " + i + " 页爬取完成，共爬取 " + count[0] + " 条新闻，" + count[1] + " 条新闻已存在");
            newsCount[0] += count[0];
            newsCount[1] += count[1];
            try {
                Thread.sleep(1000); // 慢点爬，爬一页停一秒，防止进黑名单
            } catch (InterruptedException e) {
                System.err.println("异常: " + e.getMessage());
                e.printStackTrace();
            }
        }
        return newsCount;
    }

    // 单页新闻解析方法
    public int[] crawlSinglePage(int page) throws IOException {
        int[] result = new int[2];
        result[0] = 0;
        result[1] = 0;

        // 连接网站并获取文档
        Document doc = Jsoup.connect(BASE_URL + "page_" + page + ".html")
                .userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0")
                .timeout(10000)
                .get();

        Elements excerptEl = doc.getElementsByClass("excerpt");

        // 遍历每个新闻元素并解析内容
        for (Element excerpt : excerptEl) {
            News news = new News();

            // 解析标题
            String title = excerpt.select(".focus a").attr("title");
            news.setTitle(title);

            // 跳过已存在的新闻
            if (newsDao.findByTitle(title) != null) {
                result[1]++;
                continue;
            }

            // 解析摘要
            String summary = excerpt.select(".note").text();
            news.setNote(summary);

            // 解析时间
            String time = excerpt.select(".meta time").get(1).text();
            news.setTime(time);

            // 解析标签
            String tag = excerpt.select("header .public-icon-first").text();
            if (tag.isEmpty()) {
                tag = excerpt.select(".meta a").text();
            }
            news.setTag(tag);

            // 解析封面图片
            String coverUrl = excerpt.select(".focus a img").attr("src");
            coverUrl = BASE_URL + coverUrl;
            news.setImage(coverUrl);

            // 解析新闻链接
            String newsUrl = excerpt.select(".focus a").attr("href");
            news.setUrl(newsUrl);

            // 解析新闻内容
            crawlNewsContent(newsUrl, news);

            // 保存新闻到数据库
            newsDao.save(news);
            result[0]++;
        }

        return result;
    }

    // 解析新闻内容方法
    public void crawlNewsContent(String newsUrl, News news) throws IOException {
        // 连接网站并获取文档
        Document doc = Jsoup.connect(newsUrl)
                .userAgent(
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0")
                .timeout(10000)
                .get();

        Element contentEl = doc.selectFirst(".content .article-content");

        // 相对路径转换为绝对路径
        contentEl.select("img").forEach(img -> {
            img.attr("src", BASE_URL + img.attr("src"));
        });

        //图片添加圆角类
        contentEl.select("img").forEach(img -> {
            img.addClass("rounded-3");
        });

        // 将class为keywordlink的a标签的href替换为 http://10.160.3.131:3399/home?tag= + 标签
        contentEl.select("a.keywordlink").forEach(link -> {
            link.attr("href", "http://10.160.3.131:3399/home?tag=" + link.text());
        });

        // 保存新闻内容
        news.setContent(contentEl.html());
    }
}