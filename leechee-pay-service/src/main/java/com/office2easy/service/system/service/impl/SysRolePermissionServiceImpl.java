package com.office2easy.service.system.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysRolePermission;
import com.office2easy.service.system.dao.SysRolePermissionMapper;
import com.office2easy.service.system.service.ISysRolePermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-24
 */
@Service
public class SysRolePermissionServiceImpl extends ServiceImpl<SysRolePermissionMapper, SysRolePermission> implements ISysRolePermissionService {

    @Override
    public void savePermissions(SysPermission permission, List<SysRole> userRoleList) {
        List<SysRolePermission> rolePermissions = new ArrayList<>();
        for (SysRole sysRole : userRoleList) {
            SysRolePermission sysRolePermission = new SysRolePermission();
            sysRolePermission.setPermissionId(permission.getId());
            sysRolePermission.setRoleId(sysRole.getId());
            rolePermissions.add(sysRolePermission);
        }
    }
}
