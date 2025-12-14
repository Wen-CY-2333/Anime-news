package com.example.anime_news.config;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import com.example.anime_news.pojo.User;
import com.example.anime_news.service.UserService;

public class Realm extends AuthorizingRealm {

    private UserService userService;

    public Realm(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        User user = (User) principals.getPrimaryPrincipal();
        if (user != null && user.getRole() != null) {
            authorizationInfo.addRole(user.getRole());
        }
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken arg0) throws AuthenticationException {
        String name = ((UsernamePasswordToken) arg0).getUsername();
        User user = userService.findTopByName(name);
        if (user == null) {
            throw new AuthenticationException("用户不存在");
        }
        return new SimpleAuthenticationInfo(user, user.getPassword(), getName());
    }


}
