package com.office2easy.service.system.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.office2easy.domain.system.SysUser;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-22
 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {
    //RoleVo getUserRolePermissions();
}
