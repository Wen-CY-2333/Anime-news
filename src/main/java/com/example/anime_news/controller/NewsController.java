package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.Comment;
import com.example.anime_news.service.CommentService;
import com.example.anime_news.service.NewsService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/news")
@RequiresRoles("admin")
public class NewsController {
    @Autowired
    private NewsService newsService;
    
    @Autowired
    private CommentService commentService;

    // 新闻内容页
    @GetMapping("/news/{id}")
    public ModelAndView content(@PathVariable Long id) {
        ModelAndView mv = new ModelAndView("news");
        mv.addObject("news", newsService.findById(id));
        mv.addObject("tagCountMap", newsService.countTags());
        
        // 添加评论列表
        mv.addObject("comments", commentService.findByNewsId(id));
        // 添加空评论对象，用于表单提交
        Comment comment = new Comment();
        comment.setNewsId(id);
        mv.addObject("comment", comment);

        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("isAdmin", UserUtils.isAdmin());

        return mv;
    }
    
    // 提交评论
    @PostMapping("/news/comment")
    public String submitComment(@ModelAttribute Comment comment) {
        // 设置当前用户ID
        comment.setUserId(UserUtils.getCurrentUser().getId());
        // 保存评论
        commentService.save(comment);
        // 重定向回新闻详情页
        return "redirect:/news/" + comment.getNewsId();
    }
}