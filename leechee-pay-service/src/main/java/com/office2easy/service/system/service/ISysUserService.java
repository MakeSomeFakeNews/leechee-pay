package com.office2easy.service.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.office2easy.common.entity.R;
import com.office2easy.service.system.vo.UserInfoVo;
import com.office2easy.service.system.vo.UserNavVo;
import com.office2easy.domain.system.SysPermission;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysUser;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-22
 */
public interface ISysUserService extends IService<SysUser> {
    /**
     * 添加用户
     *
     * @param sysUser 用户
     * @return
     */
    R addUser(SysUser sysUser);

    R delete(SysUser sysUser);

    R update(SysUser sysUser);

    R list(SysUser sysUser);

    IPage<SysUser> selectPage(Map<String, Object> params, SysUser sysUser);

    UserNavVo buildNav(SysPermission permission);

    UserInfoVo buildUserInfo(SysUser sysUser, List<SysRole> sysRoleList, List<SysPermission> sysPermissionList);
}
