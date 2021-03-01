package com.office2easy.service.system.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.office2easy.domain.system.SysDictType;
import com.office2easy.service.system.dao.SysDictTypeMapper;
import com.office2easy.service.system.service.ISysDictTypeService;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-27
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements ISysDictTypeService {

    @Override
    public IPage<SysDictType> selectPage(Map<String, Object> params, SysDictType dictType) {
        long current = Long.parseLong(params.get("current").toString());
        long pageSize = Long.parseLong(params.get("pageSize").toString());
        IPage<SysDictType> page = new Page<>(current, pageSize);
        LambdaQueryWrapper<SysDictType> sysUserQueryWrapper = new LambdaQueryWrapper<>();
        String dictName = dictType.getDictName();
        String code = dictType.getDictCode();
        if (!ObjectUtils.isEmpty(dictName)) {
            sysUserQueryWrapper.like(SysDictType::getDictName, dictName);
        }
        if (!ObjectUtils.isEmpty(code)) {
            sysUserQueryWrapper.like(SysDictType::getDictCode, code);
        }
        return page(page, sysUserQueryWrapper);
    }
}
