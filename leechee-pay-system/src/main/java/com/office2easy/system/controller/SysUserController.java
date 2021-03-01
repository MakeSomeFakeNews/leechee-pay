package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.service.system.vo.UserInfoVo;
import com.office2easy.service.system.vo.UserNavVo;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysUser;
import com.office2easy.service.system.service.ISysPermissionService;
import com.office2easy.service.system.service.ISysRoleService;
import com.office2easy.service.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-22
 */
@RestController
@RequestMapping("/system/sys-user")
@Slf4j
public class SysUserController extends AbsController {

    private final ISysUserService userService;

    private final ISysRoleService roleService;

    private final ISysPermissionService permissionService;

    @Autowired
    public SysUserController(ISysUserService userService, ISysRoleService roleService, ISysPermissionService permissionService) {
        this.userService = userService;
        this.roleService = roleService;
        this.permissionService = permissionService;
    }

    /**
     * 用户详情
     *
     * @return
     */
    @RequestMapping("/info")
    @Log("获取用户详情")
    public R info() {
        SysUser user = getUser();
        List<SysRole> userRoleList = roleService.getUserRoleList(user);
        List<SysPermission> rolePermissions = permissionService.getRolePermissions(userRoleList);
        UserInfoVo userInfoVo = userService.buildUserInfo(user, userRoleList, rolePermissions);
        return R.ok().data(userInfoVo);
    }

    /**
     * 用户菜单获取
     *
     * @return
     */
    @RequestMapping("/nav")
    @Log("获取用户菜单")
    public R nav() {
        List<SysRole> userRoleList = roleService.getUserRoleList(getUser());
        List<UserNavVo> userNavs = new ArrayList<>();
        userRoleList.forEach(sysRole -> {
            List<SysPermission> rolePermissions = permissionService.getRolePermissions(sysRole);
            rolePermissions.forEach(sysPermission -> {
                if (sysPermission.getPermissionType() == 1) {
                    userNavs.add(userService.buildNav(sysPermission));
                }
            });
        });
        return R.ok().data(userNavs);
    }

    @RequiresPermissions("sys:user:view")
    @RequestMapping("/getUserList")
    @Log("查看用户列表")
    public R getUserList(@RequestParam Map<String, Object> params, @RequestBody SysUser sysUser) {
        IPage<SysUser> sysUserIPage = userService.selectPage(params, sysUser);
        return R.ok().data(sysUserIPage);

    }

    @RequiresPermissions("sys:user:add")
    @RequestMapping("/add")
    @Log("添加用户")
    public R add(@RequestBody SysUser sysUser) {
        return userService.addUser(sysUser);
    }


}
