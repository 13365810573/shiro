package com.shiro.shiro.service.impl;

import com.shiro.shiro.dao.UserDao;
import com.shiro.shiro.entity.Perms;
import com.shiro.shiro.entity.Role;
import com.shiro.shiro.entity.User;
import com.shiro.shiro.service.UserService;
import com.shiro.shiro.utils.SaltUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Override
    public void register(User user) {

        //处理业务调用Dao
        //铭文密码进行md5+salt+hash散列
        //1.生成随机盐
        String salt = SaltUtils.getSalt(8);
        //2.将随机盐保存到数据库
        user.setSalt(salt);
        //3.根据铭文md5+salt+hash散列
        Md5Hash md5Hash = new Md5Hash(user.getPassword(),salt,1024);
        user.setPassword(md5Hash.toHex());
        userDao.save(user);
    }

    @Override
    public User findByUserName(String username) {
        return userDao.findByUserName(username);
    }

    @Override
    public User findRolesByUserName(String username) {
        return userDao.findRolesByUserName(username);
    }

    @Override
    public List<Perms> findPermsByRoleId(String id) {
        return userDao.findPermsByRoleId(id);
    }
}
