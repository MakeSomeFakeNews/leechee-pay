package com.office2easy.system.controller;


import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.service.system.vo.sysinfo.SysInfoVo;
import com.office2easy.service.system.service.ISysInfoService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system/sys-info")
public class SysInfoController {
    private final ISysInfoService sysInfoService;

    @Autowired
    public SysInfoController(ISysInfoService sysInfoService) {
        this.sysInfoService = sysInfoService;
    }

    @RequiresPermissions("sys:sysinfo:view")
    @RequestMapping("/sysInfo")
    @Log("获取系统资源")
    public R sysInfo() {
        SysInfoVo sysInfoVo = sysInfoService.query();
        return R.ok().data(sysInfoVo);
    }
}
