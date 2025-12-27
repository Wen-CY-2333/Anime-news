package com.example.anime_news.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.Comment;
import com.example.anime_news.service.CommentService;
import com.example.anime_news.service.LikeService;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsService newsService;
    
    @Autowired
    private CommentService commentService;
    
    @Autowired
    private LikeService likeService;

    // 新闻内容页
    @GetMapping("/{id}")
    public ModelAndView content(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("news");
        mv.addObject("news", newsService.findById(id));
        mv.addObject("tagCountMap", newsService.countTags());
        mv.addObject("likeCountMap", likeService.countLikes());
        mv.addObject("commentCountMap", newsService.countComments());
        
        mv.addObject("comments", commentService.findByNewsId(id));
        mv.addObject("isLiked", likeService.isLiked(UserUtils.getCurrentUser().getId(), id));
        
        mv.addObject("userId", UserUtils.getCurrentUser().getId());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("isAdmin", UserUtils.isAdmin());

        return mv;
    }
    
    // 提交评论
    @PostMapping("/comment")
    @ResponseBody
    public String submitComment(@ModelAttribute Comment comment) {
        // 设置提交信息
        comment.setUserName(UserUtils.getCurrentUser().getName());
        comment.setAvatar(UserUtils.getCurrentUser().getAvatar());
        // 保存评论
        commentService.save(comment);
        return "success";
    }

    //获取当前新闻的评论信息
    @GetMapping("/{id}/comments")
    @ResponseBody
    public Map<String, Object> getComments(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        result.put("comments", commentService.findByNewsId(id));
        result.put("commentCount", commentService.countByNewsId(id));
        return result;
    }
}