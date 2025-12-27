package com.example.anime_news.service;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.UserDao;
import com.example.anime_news.pojo.User;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    // 初始化管理员账号
    @PostConstruct // 应用启动时执行
    public void initAdmin() {
        User haveAdmin = userDao.findTopByRole("admin");
        if (haveAdmin == null) {
            User admin = new User();
            admin.setName("miku");
            admin.setPassword("39");
            admin.setAvatar("/img/avatar.png");
            admin.setRole("admin");
            userDao.save(admin);
        }
    }

    @CachePut(value = "user", key = "'id:' + #user.id")
    public User save(User user) {
        return userDao.save(user);
    }

    @CacheEvict(value = "user", key = "'id:' + #id")
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Cacheable(value = "user", key = "'name:' + #name")
    public User findTopByName(String name) {
        return userDao.findTopByName(name);
    }

    @Cacheable(value = "user", key = "'id:' + #id")
    public User findById(Long id) {
        return userDao.findById(id).orElse(null);
    }
    
    //分页查询所有用户
    @Cacheable(value = "user", key = "'page:' + #page + ':size:' + #size")
    public Page<User> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userDao.findAll(pageable);
    }

}