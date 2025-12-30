package com.example.anime_news.controller;

import com.example.anime_news.pojo.Like;
import com.example.anime_news.service.LikeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    // 点赞
    @PostMapping("/like")
    @ResponseBody
    public Map<String, Object> like(@ModelAttribute Like like) {
        
        Map<String, Object> result = new HashMap<>();

        if (likeService.existsByUserIdAndNewsId(like.getUserId(), like.getNewsId())) {
            result.put("error", "用户已点赞");
            return result;
        }

        likeService.save(like);
        
        // 返回点赞结果
        result.put("isLiked", true);
        result.put("likeCount", likeService.getLikeCountByNewsId(like.getNewsId()));
        
        return result;
    }

    // 取消点赞
    @PostMapping("/unlike")
    @ResponseBody
    public Map<String, Object> unlike(@RequestParam Long userId,
                                      @RequestParam Long newsId) {
        likeService.deleteByUserIdAndNewsId(userId, newsId);
        
        // 返回取消点赞结果
        Map<String, Object> result = new HashMap<>();
        result.put("isLiked", false);
        result.put("likeCount", likeService.getLikeCountByNewsId(newsId));
        
        return result;
    }
}