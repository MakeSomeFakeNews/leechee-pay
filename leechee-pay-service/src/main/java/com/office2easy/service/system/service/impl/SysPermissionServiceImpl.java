package com.office2easy.service.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysRolePermission;
import com.office2easy.service.system.dao.SysPermissionMapper;
import com.office2easy.service.system.dao.SysRolePermissionMapper;
import com.office2easy.service.system.service.ISysPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-24
 */
@Service
@Slf4j
public class SysPermissionServiceImpl extends ServiceImpl<SysPermissionMapper, SysPermission> implements ISysPermissionService {

    private final SysRolePermissionMapper rolePermissionMapper;

    @Autowired
    public SysPermissionServiceImpl(SysRolePermissionMapper rolePermissionMapper) {
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * 获取所有权限详情
     *
     * @param sysRole
     * @return
     */
    @Override
    public List<SysPermission> getRolePermissions(SysRole sysRole) {
        LambdaQueryWrapper<SysRolePermission> sysRolePermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
        sysRolePermissionLambdaQueryWrapper.eq(SysRolePermission::getRoleId, sysRole.getId());
        List<SysRolePermission> sysRolePermissions = rolePermissionMapper.selectList(sysRolePermissionLambdaQueryWrapper);
        List<SysPermission> permissions = new ArrayList<>();
        sysRolePermissions.forEach(sysRolePermission -> {
            LambdaQueryWrapper<SysPermission> sysPermissionLambdaQueryWrapper = new LambdaQueryWrapper<>();
            sysPermissionLambdaQueryWrapper.eq(SysPermission::getId, sysRolePermission.getPermissionId());
            SysPermission sysPermission = baseMapper.selectOne(sysPermissionLambdaQueryWrapper);
            permissions.add(sysPermission);
        });
        permissions.sort((o1, o2) -> {
            int diff = o1.getSort() - o2.getSort();
            if (diff > 0) {
                return 1;
            } else if (diff < 0) {
                return -1;
            }
            return 0;
        });
        return permissions;
    }

    @Override
    public List<SysPermission> getRolePermissions(List<SysRole> sysRoles) {
        List<SysPermission> permissions = new ArrayList<>();
        for (SysRole sysRole : sysRoles) {
            CollectionUtils.mergeArrayIntoCollection(getRolePermissions(sysRole).toArray(), permissions);
        }
        return permissions;
    }

    @Override
    public List<SysPermission> buildMenuList(List<SysRole> sysRoles) {
        List<SysPermission> allMenu = getRolePermissions(sysRoles);
        List<SysPermission> rootMenu = new ArrayList<>();
        for (SysPermission menu : allMenu) {
            if (menu.getPid() == 0) {
                rootMenu.add(menu);
            }
        }
        for (SysPermission menu : rootMenu) {
            List<SysPermission> child = getChild(menu.getId(), allMenu);
            menu.setChildren(child);
        }
        return rootMenu;
    }

    @Override
    public void delete(SysPermission permission) {
        baseMapper.deleteById(permission.getId());
    }

    @Override
    public List<SysPermission> buildAllMenuList() {
        List<SysPermission> allMenu = this.list();
        List<SysPermission> rootMenu = new ArrayList<>();
        for (SysPermission menu : allMenu) {
            if (menu.getPid() == 0) {
                rootMenu.add(menu);
            }
        }
        for (SysPermission menu : rootMenu) {
            List<SysPermission> child = getChild(menu.getId(), allMenu);
            menu.setChildren(child);
        }
        return rootMenu;
    }

    private List<SysPermission> getChild(Integer id, List<SysPermission> all) {
        List<SysPermission> childMenu = new ArrayList<>();

        for (SysPermission menuVo : all) {
            if (menuVo.getPid().equals(id)) {
                childMenu.add(menuVo);
            }
        }

        for (SysPermission menu : childMenu) {
            menu.setChildren(getChild(menu.getId(), all));
        }

        if (childMenu.size() == 0) {
            return new ArrayList<>();
        }
        return childMenu;
    }
}
