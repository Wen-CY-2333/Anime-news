package com.example.anime_news.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.anime_news.pojo.User;

@Repository
public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    /**
     * 根据用户名查询用户
     * @param name 用户名
     * @return 用户实体
     */
    User findTopByName(String name);

    /**
     * 根据角色查询用户
     * @param role 角色
     * @return 用户实体
     */
    User findTopByRole(String role);
}