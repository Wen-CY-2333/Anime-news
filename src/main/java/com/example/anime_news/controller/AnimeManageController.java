package com.example.anime_news.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.example.anime_news.pojo.Anime;
import org.springframework.data.domain.Page;
import com.example.anime_news.service.AnimeService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/anime-manage")
@RequiresRoles("admin")
public class AnimeManageController {
    @Autowired
    private AnimeService animeService;

    // 添加番剧
    @PostMapping("/add")
    @ResponseBody
    public void addByModel(@ModelAttribute Anime anime) {
        animeService.save(anime);
    }

    // 编辑番剧
    @PostMapping("/edit")
    @ResponseBody
    public void editByModel(@ModelAttribute Anime anime) {
        animeService.save(anime);
    }

    // 删除番剧
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        animeService.deleteById(id);
    }

    // 通过id查询番剧信息
    @GetMapping("/find/{id}")
    @ResponseBody
    public Anime getAnimeById(@PathVariable Long id) {
        return animeService.findById(id);
    }

    // 番剧管理页面
    @GetMapping("/")
    public ModelAndView animeManage(@RequestParam(defaultValue = "0") int page, 
                                   @RequestParam(defaultValue = "8") int size) {
        ModelAndView mv = new ModelAndView("anime-manage");
        
        Page<Anime> pageResult = animeService.findAll(page, size);
        mv.addObject("animeList", pageResult.getContent());

        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());

        mv.addObject("page", pageResult);
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }
}