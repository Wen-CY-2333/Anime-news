package com.example.anime_news.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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
    
    @Autowired
    private CommentService commentService;

    @CachePut(value = "news", key = "'id:' + #news.id")
    public News save(News news) {
        return newsDao.save(news);
    }

    @CacheEvict(value = "news", key = "'id:' + #id")
    public void deleteById(Long id) {
        newsDao.deleteById(id);
    }

    @CacheEvict(value = "news", allEntries = true)
    public void deleteAll() {
        newsDao.deleteAll();
    }

    @Cacheable(value = "news", key = "'title:' + #title")
    public News findByTitle(String title) {
        return newsDao.findByTitle(title);
    }

    @Cacheable(value = "news", key = "'id:' + #id")
    public News findById(Long id) {
        return newsDao.findById(id).orElse(null);
    }

    // 分页查询所有新闻
    @Cacheable(value = "news", key = "'page:' + #page + ':size:' + #size")
    public Page<News> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsDao.findAll(pageable);
    }

    // 获取所有新闻的评论数映射
    @Cacheable(value = "news", key = "'count:comments'")
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
    @Cacheable(value = "news", key = "'count:tags'")
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
    @Cacheable(value = "news", key = "'search:keyword:' + #keyword + ':tag:' + #tag + ':page:' + #page + ':size:' + #size")
    public Page<News> search(String keyword, String tag, int page, int size) {
        Sort sort = Sort.by("visits").descending().and(Sort.by("time").descending());
        Pageable pageable = PageRequest.of(page, size, sort);
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
}