package com.example.anime_news.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.anime_news.pojo.News;

import java.io.IOException;

@Service
public class SpiderService {
    @Autowired
    private NewsService newsService;

    // 梦域动漫
    public static final String BASE_URL = "https://www.moelove.cn/";
    
    // 爬取指定页的新闻
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
                Thread.sleep(1000);     // 慢点爬，爬一页停一秒，防止进黑名单
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
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
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0")
                .timeout(10000)
                .get();

        Elements excerptEl = doc.getElementsByClass("excerpt");
        
        // 遍历每个新闻元素并解析内容
        for (Element excerpt : excerptEl) {
            News news = new News();
            
            // 解析标题
            String title = excerpt.select(".focus a").attr("title");
            news.setTitle(title);
            
            //跳过已存在的新闻
            if (newsService.findByTitle(title) != null) {
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
            newsService.save(news);
            result[0]++;
        }

        return result;
    }

    // 解析新闻内容方法
    public void crawlNewsContent(String newsUrl, News news) throws IOException {
        // 连接网站并获取文档
        Document doc = Jsoup.connect(newsUrl)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/143.0.0.0 Safari/537.36 Edg/143.0.0.0")
                .timeout(10000)
                .get();
        
        Element contentEl = doc.selectFirst(".content .article-content");

        // 相对路径转换为绝对路径
        contentEl.select("img").forEach(img -> {
            img.attr("src", BASE_URL + img.attr("src"));
        });

        //保存新闻内容
        news.setContent(contentEl.html());
    }
    
}
