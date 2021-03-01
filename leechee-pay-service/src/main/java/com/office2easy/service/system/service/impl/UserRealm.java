package com.office2easy.service.system.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.office2easy.common.shiro.JWTToken;
import com.office2easy.common.utils.JwtUtils;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysUser;
import com.office2easy.service.system.service.ISysPermissionService;
import com.office2easy.service.system.service.ISysRoleService;
import com.office2easy.service.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Slf4j
public class UserRealm extends AuthorizingRealm {

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysPermissionService permissionService;


    @Override
    public boolean supports(AuthenticationToken authenticationToken) {
        return authenticationToken instanceof JWTToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SysUser sysUser = (SysUser) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        List<SysRole> userRoleList = roleService.getUserRoleList(sysUser);
        for (SysRole sysRole : userRoleList) {
            simpleAuthorizationInfo.addRole(sysRole.getRoleCode());
            List<SysPermission> rolePermissions = permissionService.getRolePermissions(sysRole);
            for (SysPermission rolePermission : rolePermissions) {
                if (rolePermission.getPermissionType() == 2) {
                    simpleAuthorizationInfo.addStringPermission(rolePermission.getPermission());
                }
            }
        }
        return simpleAuthorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) {
        String token = authenticationToken.getCredentials().toString();
        String username = JwtUtils.getUsername(token);
        if (StringUtils.isBlank(username)) {
            throw new AuthenticationException("token非法无效!");
        }
        SysUser sysUser = userService.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, username));
        if (sysUser == null) {
            throw new AuthenticationException("用户不存在!");
        }// 判断用户状态
        if (sysUser.getStatus() == 1) {
            throw new AuthenticationException("账号已被禁用,请联系管理员!");
        }
        return new SimpleAuthenticationInfo(sysUser, token, getName());
    }
}
