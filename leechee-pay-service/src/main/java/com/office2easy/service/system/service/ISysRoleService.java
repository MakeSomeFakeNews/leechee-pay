package com.office2easy.service.system.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.office2easy.domain.system.SysRole;
import com.office2easy.domain.system.SysUser;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-24
 */
public interface ISysRoleService extends IService<SysRole> {
    List<SysRole> getUserRoleList(SysUser sysUser);
}
