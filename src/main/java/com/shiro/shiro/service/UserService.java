package com.shiro.shiro.service;

import com.shiro.shiro.entity.Perms;
import com.shiro.shiro.entity.Role;
import com.shiro.shiro.entity.User;

import java.util.List;

public interface UserService {
    //注册用户
    void register(User user);
    //根据用户名查询业务的方法
    User findByUserName(String username);

    User findRolesByUserName(String username);

    //根据权限的id查询权限集合
    List<Perms>findPermsByRoleId(String id);
}
