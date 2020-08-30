package com.shiro.shiro.dao;

import com.shiro.shiro.entity.Perms;
import com.shiro.shiro.entity.Role;
import com.shiro.shiro.entity.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.mybatis.spring.annotation.MapperScan;

import java.util.List;

@Mapper
public interface UserDao {


    void save(User user);

    @Select("select id,username,password,salt from t_user where username = #{username}")
    User findByUserName(String username);


    User findRolesByUserName(String username);

    //根据权限的id查询权限集合
    List<Perms>findPermsByRoleId(String id);

}
