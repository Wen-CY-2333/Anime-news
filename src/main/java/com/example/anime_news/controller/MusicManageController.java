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

import com.example.anime_news.pojo.Music;
import org.springframework.data.domain.Page;
import com.example.anime_news.service.MusicService;
import com.example.anime_news.utils.UserUtils;

@Controller
@RequestMapping("/music-manage")
@RequiresRoles("admin")
public class MusicManageController {
    @Autowired
    private MusicService musicService;

    // 添加音乐
    @PostMapping("/add")
    @ResponseBody
    public void addByModel(@ModelAttribute Music music) {
        musicService.save(music);
    }

    // 编辑音乐
    @PostMapping("/edit")
    @ResponseBody
    public void editByModel(@ModelAttribute Music music) {
        musicService.save(music);
    }

    // 删除音乐
    @PostMapping("/delete/{id}")
    @ResponseBody
    public void delete(@PathVariable Long id) {
        musicService.deleteById(id);
    }

    // 通过id查询音乐信息
    @GetMapping("/find/{id}")
    @ResponseBody
    public Music getMusicById(@PathVariable Long id) {
        return musicService.findById(id);
    }

    // 音乐管理页面
    @GetMapping("/")
    public ModelAndView musicManage(@RequestParam(defaultValue = "0") int page, 
                                   @RequestParam(defaultValue = "8") int size) {
        ModelAndView mv = new ModelAndView("music-manage");
        
        Page<Music> pageResult = musicService.findAll(page, size);
        mv.addObject("musicList", pageResult.getContent());

        mv.addObject("userName", UserUtils.getCurrentUser().getName());
        mv.addObject("avatar", UserUtils.getCurrentUser().getAvatar());

        mv.addObject("page", pageResult);
        mv.addObject("currentPage", page);
        mv.addObject("pageSize", size);
        return mv;
    }
}