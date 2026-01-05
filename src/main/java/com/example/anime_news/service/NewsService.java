package com.example.anime_news.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.NewsDao;
import com.example.anime_news.pojo.Comment;
import com.example.anime_news.pojo.News;

@Service
public class NewsService {
    @Autowired
    private NewsDao newsDao;

    @Autowired
    private CommentService commentService;

    @CachePut(value = "news", key = "#news.id")
    public News save(News news) {
        return newsDao.save(news);
    }

    @CacheEvict(value = "news", key = "#id")
    public void deleteById(Long id) {
        newsDao.deleteById(id);
    }

    @CacheEvict(value = "news", allEntries = true)
    public void deleteAll() {
        newsDao.deleteAll();
    }

    public News findByTitle(String title) {
        return newsDao.findByTitle(title);
    }

    @Cacheable(value = "news", key = "#id")
    public News findById(Long id) {
        return newsDao.findById(id).orElse(null);
    }

    // 分页查询所有新闻
    public Page<News> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsDao.findAll(pageable);
    }

    // 获取所有新闻的评论数映射
    public Map<Long, Long> countComments() {
        List<News> newsList = newsDao.findAll();
        Map<Long, Long> commentCountMap = new HashMap<>();

        for (News news : newsList) {
            long count = commentService.countByNewsId(news.getId());
            commentCountMap.put(news.getId(), count);
        }

        return commentCountMap;
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

    //多字段跨表搜索，合并标签检索和分页功能
    public Page<News> search(String keyword, String tag, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "visits", "time");
        Pageable pageable = PageRequest.of(page, size, sort);

        // lambda表达式，root：查询的根对象（实体News），query：设置查询结构（distinct、orderBy...），cb：查询构造器
        Specification<News> spec = (root, query, cb) -> {
            // 开启distinct，去重
            query.distinct(true);

            // 构建查询的条件，每个 Predicate 对应 WHERE 子句中的一个条件
            List<Predicate> predicates = new ArrayList<>();

            // 1. 标签精确匹配（可选）
            if (tag != null && !tag.isEmpty()) {
                predicates.add(cb.equal(root.get("tag"), tag.trim()));
            }

            // 2. 关键字跨表多字段搜索
            if (keyword != null && !keyword.isEmpty()) {
                String trimmedKeyword = keyword.trim();
                String likePattern = "%" + trimmedKeyword + "%";

                // 新闻自身字段：title、note、tag 的 OR 条件
                List<Predicate> newsPredicates = new ArrayList<>();
                newsPredicates.add(cb.like(root.get("title"), likePattern));
                newsPredicates.add(cb.like(root.get("note"), likePattern));
                newsPredicates.add(cb.like(root.get("tag"), likePattern));

                Predicate newsMatch = cb.or(newsPredicates.toArray(new Predicate[0]));

                // 跨表：评论内容匹配、评论者匹配
                Join<News, Comment> commentJoin = root.join("comments", JoinType.LEFT);
                Predicate commentMatch = cb.like(commentJoin.get("commentContent"), likePattern);
                Predicate commenterMatch = cb.like(commentJoin.get("userName"), likePattern);

                // 关键逻辑：新闻字段 OR 评论内容 OR 评论者，只要命中一个即返回该新闻
                Predicate keywordMatch = cb.or(newsMatch, commentMatch, commenterMatch);
                predicates.add(keywordMatch);
            }

            // 所有条件 AND 连接
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return newsDao.findAll(spec, pageable);
    }
}