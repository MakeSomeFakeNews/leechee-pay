package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.common.utils.ShiroUtils;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysRolePermission;
import com.office2easy.domain.system.SysUser;
import com.office2easy.service.system.service.ISysPermissionService;
import com.office2easy.service.system.service.ISysRolePermissionService;
import com.office2easy.service.system.service.ISysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-24
 */
@RestController
@RequestMapping("/system/sys-permission")
public class SysPermissionController extends AbsController {
    private final ISysPermissionService permissionService;
    private final ISysRoleService roleService;
    private final ISysRolePermissionService rolePermissionService;
    private final ShiroUtils shiroUtils;

    @Autowired
    public SysPermissionController(ISysPermissionService permissionService, ISysRoleService roleService, ISysRolePermissionService rolePermissionService, ShiroUtils shiroUtils) {
        this.permissionService = permissionService;
        this.roleService = roleService;
        this.rolePermissionService = rolePermissionService;
        this.shiroUtils = shiroUtils;
    }

    @RequestMapping("/list")
    @RequiresPermissions("sys:permission:view")
    @Log("查看菜单列表")
    public R list() {
        List<SysRole> userRoleList = roleService.getUserRoleList(getUser());
        List<SysPermission> rolePermissions = permissionService.buildMenuList(userRoleList);
        return R.ok().data(rolePermissions);
    }

    @RequestMapping("/add")
    @RequiresPermissions("sys:permission:add")
    @Log("添加菜单")
    public R add(@RequestBody SysPermission permission) {
        SysUser user = getUser();
        permission.setCreateBy(user.getUsername());
        permission.setCreateTime(LocalDateTime.now());
        permissionService.save(permission);
        List<SysRole> userRoleList = roleService.getUserRoleList(user);
        rolePermissionService.savePermissions(permission,userRoleList);
        shiroUtils.removePermission();
        return R.ok().data(permission);
    }

    @RequestMapping("/delete")
    @RequiresPermissions("sys:permission:del")
    @Log("删除菜单")
    public R delete(@RequestBody SysPermission permission) {
        permissionService.delete(permission);
        //还需要删除权限
        LambdaQueryWrapper<SysRolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRolePermission::getPermissionId, permission.getId());
        rolePermissionService.remove(queryWrapper);
        return R.ok().data(permission);
    }

    @RequestMapping("/update")
    @RequiresPermissions("sys:permission:update")
    @Log("更新菜单")
    public R update(@RequestBody SysPermission permission) {
        permissionService.updateById(permission);
        shiroUtils.removePermission();
        return R.ok().data(permission);
    }
}
