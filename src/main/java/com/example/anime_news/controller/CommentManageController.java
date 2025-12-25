package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.Comment;
import com.example.anime_news.service.CommentService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/comment-manage")
@RequiresRoles("admin")
public class CommentManageController {
    @Autowired
    private CommentService commentService;
    
    // 删除评论
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        commentService.deleteById(id);
    }
    
    // 评论管理页面
    @GetMapping("/")
    public ModelAndView commentManage(@RequestParam(defaultValue = "0") int page, 
                               @RequestParam(defaultValue = "8") int size) {
        ModelAndView mv = new ModelAndView("comment_manage");
        Pageable pageable = PageRequest.of(page, size);
        Page<Comment> commentPage = commentService.findAll(pageable);
        
        mv.addObject("commentList", commentPage.getContent());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("page", commentPage);
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }
}
