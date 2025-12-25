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

import com.example.anime_news.pojo.Like;
import com.example.anime_news.service.LikeService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/like-manage")
@RequiresRoles("admin")
public class LikeManageController {
    @Autowired
    private LikeService likeService;
    
    // 删除点赞
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        likeService.deleteById(id);
    }
    
    // 点赞管理页面
    @GetMapping("/")
    public ModelAndView likeManage(@RequestParam(defaultValue = "0") int page, 
                               @RequestParam(defaultValue = "8") int size) {
        ModelAndView mv = new ModelAndView("like_manage");
        Pageable pageable = PageRequest.of(page, size);
        Page<Like> likePage = likeService.findAll(pageable);
        
        mv.addObject("likeList", likePage.getContent());
        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());
        mv.addObject("page", likePage);
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }
}
