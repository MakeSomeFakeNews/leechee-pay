package com.office2easy.service.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-24
 */
public interface ISysPermissionService extends IService<SysPermission> {

    List<SysPermission> getRolePermissions(SysRole sysRole);

    List<SysPermission> getRolePermissions(List<SysRole> sysRoles);

    List<SysPermission> buildMenuList(List<SysRole> sysRoles);


    void delete(SysPermission permission);

    List<SysPermission> buildAllMenuList();
}
