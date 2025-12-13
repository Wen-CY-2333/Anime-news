package com.example.anime_news.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.anime_news.pojo.User;
import com.example.anime_news.service.LikeService;
import com.example.anime_news.utils.UserUtils;

@Controller
public class LikeController {
    
    @Autowired
    private LikeService likeService;
    
    // 点赞或取消点赞
    @RequestMapping("/like")
    @ResponseBody
    public Map<String, Object> like(Long newsId) {
        Map<String, Object> result = new HashMap<>();
        // 获取当前登录用户
        User currentUser = UserUtils.getCurrentUser();
        if (currentUser == null) {
            result.put("success", false);
            result.put("message", "请先登录");
            return result;
        }
        
        // 执行点赞或取消点赞
        boolean isLiked = likeService.like(currentUser.getId(), newsId);
        // 获取更新后的点赞数
        long likeCount = likeService.getLikeCount(newsId);
        
        result.put("success", true); // 表示操作成功
        result.put("isLiked", isLiked); // 表示当前用户是否已点赞
        result.put("likeCount", likeCount); // 更新后的点赞数
        return result;
    }
}
