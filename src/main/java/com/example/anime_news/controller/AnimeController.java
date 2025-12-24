package com.example.anime_news.controller;

import com.example.anime_news.pojo.Anime;
import com.example.anime_news.service.AnimeService;
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
@RequestMapping("/anime")
public class AnimeController {
    
    @Autowired
    private AnimeService animeService;
    
    // 展示番剧
    @GetMapping("/")
    public ModelAndView animeList(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        ModelAndView mv = new ModelAndView("anime");
        
        // 根据筛选条件获取番剧
        Page<Anime> animePage = getFilteredAnime(filter, page, size);
        mv.addObject("animeList", animePage.getContent());
        mv.addObject("page", animePage);
        
        // 添加分页信息
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        mv.addObject("currentFilter", filter);
        
        mv.addObject("isAdmin", UserUtils.isAdmin());
        
        return mv;
    }
    
    // 筛选接口
    @GetMapping("/filter")
    @ResponseBody
    public Page<Anime> filterAnime(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {
        return getFilteredAnime(filter, page, size);
    }
    
    // 获取筛选后的番剧列表
    private Page<Anime> getFilteredAnime(String filter, int page, int size) {
        if (filter == null || "all".equals(filter)) {
            return animeService.findByWeek(null, page, size); // 查询所有番剧
        } else if ("today".equals(filter)) {
            return animeService.findTodayAnime(page, size); // 今日更新
        } else {
            // 根据星期筛选
            try {
                Integer week = Integer.parseInt(filter);
                return animeService.findByWeek(week, page, size);
            } catch (NumberFormatException e) {
                return animeService.findByWeek(null, page, size); // 默认为查询所有
            }
        }
    }
}