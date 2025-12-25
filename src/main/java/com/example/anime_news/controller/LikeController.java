package com.example.anime_news.controller;

import com.example.anime_news.pojo.Like;
import com.example.anime_news.service.LikeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LikeController {

    @Autowired
    private LikeService likeService;

    // 点赞
    @PostMapping("/like")
    @ResponseBody
    public void likeBymodel(@ModelAttribute Like like) {
        likeService.save(like);
    }

    // 取消点赞
    @PostMapping("/unlike")
    @ResponseBody
    public void unlike(@RequestParam Long userId,
                       @RequestParam Long newsId) {
        likeService.deleteByUserIdAndNewsId(userId, newsId);
    }
}