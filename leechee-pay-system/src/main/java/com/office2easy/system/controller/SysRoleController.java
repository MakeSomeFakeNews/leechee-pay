package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.common.utils.ShiroUtils;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysRolePermission;
import com.office2easy.domain.system.SysUserRole;
import com.office2easy.service.system.service.ISysPermissionService;
import com.office2easy.service.system.service.ISysRolePermissionService;
import com.office2easy.service.system.service.ISysRoleService;
import com.office2easy.service.system.service.ISysUserRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-24
 */
@RestController
@RequestMapping("/system/sys-role")
@Slf4j
public class SysRoleController {
    private final ISysRoleService roleService;

    private final ISysPermissionService permissionService;

    private final ISysRolePermissionService rolePermissionService;

    private final ISysUserRoleService userRoleService;

    private final ShiroUtils shiroUtils;

    @Autowired
    public SysRoleController(ISysRoleService roleService, ISysPermissionService permissionService, ISysRolePermissionService rolePermissionService, ISysUserRoleService userRoleService, ShiroUtils shiroUtils) {
        this.roleService = roleService;
        this.permissionService = permissionService;
        this.rolePermissionService = rolePermissionService;
        this.userRoleService = userRoleService;
        this.shiroUtils = shiroUtils;
    }

    @RequestMapping("/onwKeys")
    public R onwKeys(@RequestBody SysRole role) {
        List<SysPermission> rolePermissions = permissionService.getRolePermissions(role);
        List<Integer> onwKeys = new ArrayList<>();
        for (SysPermission rolePermission : rolePermissions) {
            onwKeys.add(rolePermission.getId());
        }
        return R.ok().data(onwKeys);
    }

    @RequestMapping("/list")
    @RequiresPermissions("sys:role:view")
    @Log("查看角色列表")
    public R list() {
        Map<String, Object> roleMap = new HashMap<>();
        List<SysRole> roleList = roleService.list();
        List<SysPermission> permissionList = permissionService.list();
        roleMap.put("roleList", roleList);
        roleMap.put("permissionList", permissionList);
        return R.ok().data(roleMap);
    }

    @RequestMapping("/saveOrUpdate")
    @RequiresPermissions({"sys:role:del", "sys:role:update"})
    @Log("更新角色")
    public R saveOrUpdate(@RequestBody SysRole sysRole) {
        boolean saveOrUpdate = roleService.saveOrUpdate(sysRole);
        if (sysRole.getRoleList() != null) {
            LambdaQueryWrapper<SysRolePermission> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(SysRolePermission::getRoleId, sysRole.getId());
            rolePermissionService.remove(queryWrapper);
            List<SysRolePermission> rolePermissions = new ArrayList<>();
            for (Integer permissionId : sysRole.getRoleList()) {
                SysRolePermission sysRolePermission = new SysRolePermission();
                sysRolePermission.setRoleId(sysRole.getId());
                sysRolePermission.setPermissionId(permissionId);
                rolePermissions.add(sysRolePermission);
            }
            rolePermissionService.saveBatch(rolePermissions);
            shiroUtils.removePermission();
        }
        return R.ok();
    }

    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:del")
    @Log("删除角色")
    public R delete(@RequestBody SysRole sysRole) {
        LambdaQueryWrapper<SysUserRole> userRoleLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userRoleLambdaQueryWrapper.eq(SysUserRole::getRoleId, sysRole.getId());
        SysUserRole userRole = userRoleService.getOne(userRoleLambdaQueryWrapper);
        if (!ObjectUtils.isEmpty(userRole)) {
            return R.error().message("不能删除该角色,请先移除该角色下用户");
        }
        LambdaQueryWrapper<SysRolePermission> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysRolePermission::getRoleId, sysRole.getId());
        rolePermissionService.remove(queryWrapper);
        roleService.removeById(sysRole.getId());
        return R.ok();
    }

}
