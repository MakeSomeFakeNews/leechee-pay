package com.office2easy.service.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office2easy.domain.system.SysLog;
import com.office2easy.service.system.dao.SysLogMapper;
import com.office2easy.service.system.service.ISysLogService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-28
 */
@Service
public class SysLogServiceImpl extends ServiceImpl<SysLogMapper, SysLog> implements ISysLogService {

    @Override
    public IPage<SysLog> selectPage(Map<String, Object> params, SysLog sysLog) {
        long current = Long.parseLong(params.get("current").toString());
        long pageSize = Long.parseLong(params.get("pageSize").toString());
        IPage<SysLog> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<SysLog> sysLogLambdaQueryWrapper = new LambdaQueryWrapper<>();
        String username = sysLog.getUsername();
        String ip = sysLog.getIp();
        if (!ObjectUtils.isEmpty(username)) {
            sysLogLambdaQueryWrapper.like(SysLog::getUsername, username);
        }
        if (!ObjectUtils.isEmpty(ip)) {
            sysLogLambdaQueryWrapper.like(SysLog::getIp, ip);
        }
        List<String> col = new ArrayList<>();
        col.add("create_time");
        return page(page, sysLogLambdaQueryWrapper.orderByDesc(true, SysLog::getCreateTime));
    }
}
