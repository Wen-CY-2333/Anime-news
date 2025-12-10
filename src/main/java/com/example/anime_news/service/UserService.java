package com.example.anime_news.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.anime_news.dao.UserDao;
import com.example.anime_news.pojo.User;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public User save(User user) {
        return userDao.save(user);
    }

    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public User findTopByName(String name) {
        return userDao.findTopByName(name);
    }
}