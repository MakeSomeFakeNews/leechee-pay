package com.office2easy.service.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office2easy.domain.system.SysDict;
import com.office2easy.service.system.dao.SysDictMapper;
import com.office2easy.service.system.service.ISysDictService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-27
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public IPage<SysDict> selectPage(Map<String, Object> params, SysDict dict) {
        long current = Long.parseLong(params.get("current").toString());
        long pageSize = Long.parseLong(params.get("pageSize").toString());
        IPage<SysDict> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<SysDict> sysUserQueryWrapper = new LambdaQueryWrapper<>();
        String value = dict.getValue();
        if (!ObjectUtils.isEmpty(value)) {
            sysUserQueryWrapper.like(SysDict::getValue, value);
        }
        return page(page, sysUserQueryWrapper);
    }
}
