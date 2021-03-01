package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.domain.system.SysLog;
import com.office2easy.service.system.service.ISysLogService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-28
 */
@RestController
@RequestMapping("/system/sys-log")
public class SysLogController {

    private final ISysLogService logService;

    @Autowired
    public SysLogController(ISysLogService logService) {
        this.logService = logService;
    }

    @RequiresPermissions("sys:log:view")
    @RequestMapping("/list")
    @Log("查看系统操作日志")
    public R getUserList(@RequestParam Map<String, Object> params, @RequestBody SysLog sysLog) {
        IPage<SysLog> sysLogIPage = logService.selectPage(params, sysLog);
        return R.ok().data(sysLogIPage);

    }
}
