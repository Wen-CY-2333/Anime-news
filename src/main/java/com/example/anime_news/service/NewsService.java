package com.example.anime_news.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.News;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    public News save(News news) {
        return newsDao.save(news);
    }

    public void deleteById(Long id) {
        newsDao.deleteById(id);
    }

    public void deleteAll() {
        newsDao.deleteAll();
    }

    public News findByTitle(String title) {
        return newsDao.findByTitle(title);
    }

    public News findById(Long id) {
        return newsDao.findById(id).orElse(null);
    }

    // 分页查询所有新闻
    public Page<News> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return newsDao.findAll(pageable);
    }

    // 统计所有标签及其出现次数
    public Map<String, Long> countTags() {
        List<News> newsList = newsDao.findAll();
        Map<String, Long> tagCountMap = new HashMap<>();

        for (News news : newsList) {
            String tag = news.getTag();
            if (tag != null && !tag.isEmpty()) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0L) + 1);
            }
        }

        return tagCountMap;
    }

    // 使用Specification实现多字段搜索，整合分页和标签检索
    public Page<News> search(String keyword, String tag, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("time").descending());
        return newsDao.findAll((root, query, criteriaBuilder) -> {
            javax.persistence.criteria.Predicate predicate = criteriaBuilder.conjunction();

            // 标签搜索
            if (tag != null && !tag.isEmpty()) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("tag"), tag));
            }

            // 关键字搜索，支持多字段模糊查询
            if (keyword != null && !keyword.isEmpty()) {
                String likeKeyword = "%" + keyword + "%";
                javax.persistence.criteria.Predicate keywordPredicate = criteriaBuilder.or(
                        criteriaBuilder.like(root.get("title"), likeKeyword),
                        criteriaBuilder.like(root.get("note"), likeKeyword),
                        criteriaBuilder.like(root.get("content"), likeKeyword),
                        criteriaBuilder.like(root.get("tag"), likeKeyword));
                predicate = criteriaBuilder.and(predicate, keywordPredicate);
            }

            return predicate;
        }, pageable);
    }

    // 梦域动漫地址
    public static final String BASE_URL = "https://www.moelove.cn/";

    // 爬取指新闻
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

        // 保存新闻内容
        news.setContent(contentEl.html());
    }
}