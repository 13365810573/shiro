package com.shiro.shiro.shiro.realms;

import com.shiro.shiro.entity.Perms;
import com.shiro.shiro.entity.Role;
import com.shiro.shiro.entity.User;
import com.shiro.shiro.service.UserService;
import com.shiro.shiro.utils.ApplicationContextUtils;
import org.apache.commons.collections.ListUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;

import java.util.List;

//自定义realm
public class CustomerRealm extends AuthorizingRealm {


    /**
     *
     * 授权
     * @param principal
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principal) {
        String primaryPrincipal = (String) principal.getPrimaryPrincipal();
        System.out.println("调用权限验证"+primaryPrincipal);

        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User user = userService.findRolesByUserName(primaryPrincipal);
        System.out.println(user);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //授权角色信息
        if (!CollectionUtils.isEmpty(user.getRoles())){

            user.getRoles().forEach(role -> {
                simpleAuthorizationInfo.addRole(role.getName());
                //二级权限信息
                List<Perms> perms = userService.findPermsByRoleId(role.getId());
                if (!CollectionUtils.isEmpty(perms)){
                    perms.forEach(perm -> {
                        simpleAuthorizationInfo.addStringPermission(perm.getName());
                    });
                }
            });
            return simpleAuthorizationInfo;
        }
        return simpleAuthorizationInfo;
    }

    /**
     * 凭证
     * @param token
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String principal = (String) token.getPrincipal();
        //在工厂中获取service对象
        UserService userService = (UserService) ApplicationContextUtils.getBean("userService");
        User user = userService.findByUserName(principal);
        if (!ObjectUtils.isEmpty(user)){
            return new SimpleAuthenticationInfo(user.getUsername(),user.getPassword(), ByteSource.Util.bytes(user.getSalt()),this.getName());
        }
        System.out.println("=================================");
        return null;
    }
}
