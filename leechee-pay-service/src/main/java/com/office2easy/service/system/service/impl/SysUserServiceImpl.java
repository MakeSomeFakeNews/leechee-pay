package com.office2easy.service.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office2easy.common.entity.R;
import com.office2easy.service.system.vo.*;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysUser;
import com.office2easy.service.system.dao.SysUserMapper;
import com.office2easy.service.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-22
 */
@Service
@Slf4j
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements ISysUserService {


    @Override
    public R addUser(SysUser sysUser) {
        SysUser user = this.getOne(new QueryWrapper<SysUser>().lambda().eq(SysUser::getUsername, sysUser.getUsername()));
        if (!ObjectUtils.isEmpty(user)) {
            return R.error().message("用户名已存在，请重试");
        }
        String salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        Md5Hash md5Hash = new Md5Hash(sysUser.getPassword(), salt, 1024);
        sysUser.setSalt(salt);
        sysUser.setPassword(md5Hash.toHex());
        sysUser.setCreateTime(LocalDateTime.now());
        this.save(sysUser);
        return R.ok();
    }

    @Override
    public R delete(SysUser sysUser) {
        return null;
    }

    @Override
    public R update(SysUser sysUser) {
        return null;
    }

    @Override
    public R list(SysUser sysUser) {
        return null;
    }

    @Override
    public UserNavVo buildNav(SysPermission permission) {
        UserNavVo userNav = new UserNavVo();
        userNav.setId(permission.getId());
        userNav.setName(permission.getComponentName());
        userNav.setComponent(permission.getComponent());
        userNav.setParentId(permission.getPid());
        userNav.setRedirect(permission.getRedirect());
        userNav.setPath(permission.getPath());
        MetaVo meta = new MetaVo();
        meta.setIcon(permission.getIcon());
        meta.setShow(true);
        meta.setLink(permission.getLink());
        meta.setTitle(permission.getPermissionName());
        userNav.setMeta(meta);
        return userNav;
    }

    @Override
    public UserInfoVo buildUserInfo(SysUser sysUser, List<SysRole> sysRoleList, List<SysPermission> sysPermissionList) {
        //用户基本信息
        UserInfoVo userInfo = new UserInfoVo();
        userInfo.setId(sysUser.getId());
        userInfo.setName(sysUser.getUsername());
        userInfo.setUsername(sysUser.getUsername());
        userInfo.setStatus(sysUser.getStatus());
        userInfo.setTelephone(sysUser.getPhone());
        userInfo.setCreteTime(sysUser.getCreateTime());
        userInfo.setDeleted(sysUser.getIsDelete());
        SysRole sysRole = sysRoleList.get(0);
        userInfo.setRoleId(sysRole.getRoleCode());
        RoleVo roleVo = new RoleVo();
        roleVo.setId(sysRole.getRoleCode());
        roleVo.setName(sysRole.getRoleName());
        roleVo.setDescribe(sysRole.getDescript());
        roleVo.setStatus(sysRole.getStatus());
        roleVo.setCreatorId(sysRole.getCreateBy());
        roleVo.setCreateTime(sysRole.getCreateTime());
        roleVo.setDeleted(sysRole.getIsDelete());
        List<PermissionVo> permissionVos = new ArrayList<>();
        //组装路由权限
        for (SysPermission sysPermission : sysPermissionList) {
            if (sysPermission.getPermissionType() != 1) {
                continue;
            }
            PermissionVo permissionVo = new PermissionVo();
            permissionVo.setRoleId(sysRole.getRoleCode());
            permissionVo.setPermissionId(sysPermission.getPermission());
            permissionVo.setPermissionName(sysPermission.getPermissionName());
            List<ActionEntitySetVo> actionEntitySet = new ArrayList<>();
            for (SysPermission permission : sysPermissionList) {
                if (permission.getPermissionType() == 2) {
                    if (permission.getPid().equals(sysPermission.getId())) {
                        ActionEntitySetVo actionEntitySetVo = new ActionEntitySetVo();
                        actionEntitySetVo.setAction(permission.getPermission());
                        actionEntitySetVo.setDescribe(permission.getDescript());
                        actionEntitySetVo.setDefaultCheck(false);
                        actionEntitySet.add(actionEntitySetVo);
                    }
                }
            }
            permissionVo.setActionEntitySet(actionEntitySet);
            permissionVos.add(permissionVo);
        }
        roleVo.setPermissions(permissionVos);
        userInfo.setRole(roleVo);
        return userInfo;
    }

    @Override
    public IPage<SysUser> selectPage(Map<String, Object> params, SysUser sysUser) {
        long current = Long.parseLong(params.get("current").toString());
        long pageSize = Long.parseLong(params.get("pageSize").toString());
        IPage<SysUser> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<SysUser> sysUserQueryWrapper = new LambdaQueryWrapper<>();
        String username = sysUser.getUsername();
        Integer status = sysUser.getStatus();
        if (!ObjectUtils.isEmpty(username)) {
            sysUserQueryWrapper.like(SysUser::getUsername, username);
        }
        if (!ObjectUtils.isEmpty(status)) {
            sysUserQueryWrapper.like(SysUser::getStatus, status);
        }
        return page(page, sysUserQueryWrapper);
    }
}
