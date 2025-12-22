package com.example.anime_news.controller;

import com.example.anime_news.pojo.Music;
import com.example.anime_news.service.MusicService;
import com.example.anime_news.utils.UserUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/music")
public class MusicController {
    
    @Autowired
    private MusicService musicService;
    
    // 展示音乐列表
    @GetMapping("/")
    public ModelAndView musicList(
            @RequestParam(required = false) Integer cateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        ModelAndView mv = new ModelAndView("music");
        
        // 根据筛选条件获取音乐
        Page<Music> musicPage = musicService.findByCateId(cateId, page, size);
        mv.addObject("musicList", musicPage.getContent());
        mv.addObject("page", musicPage);
        
        // 添加分页信息
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        mv.addObject("currentCateId", cateId);
        
        mv.addObject("isAdmin", UserUtils.isAdmin());
        
        return mv;
    }
    
    // 筛选接口
    @GetMapping("/filter")
    @ResponseBody
    public Page<Music> filterMusic(
            @RequestParam(required = false) Integer cateId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return musicService.findByCateId(cateId, page, size);
    }
}