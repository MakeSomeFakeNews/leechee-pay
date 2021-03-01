package com.office2easy.common.utils;


import com.office2easy.domain.system.SysUser;
import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {
    public SysUser getUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }
}
