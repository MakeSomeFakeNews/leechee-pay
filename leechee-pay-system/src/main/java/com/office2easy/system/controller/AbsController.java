package com.office2easy.system.controller;


import com.office2easy.domain.system.SysUser;
import org.apache.shiro.SecurityUtils;

public abstract class AbsController {
    protected SysUser getUser() {
        return (SysUser) SecurityUtils.getSubject().getPrincipal();
    }

    protected int getUserId() {
        return getUser().getId();
    }


}
