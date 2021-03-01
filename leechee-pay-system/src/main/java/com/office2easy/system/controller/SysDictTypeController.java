package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.domain.system.SysDictType;
import com.office2easy.service.system.service.ISysDictTypeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-27
 */
@Slf4j
@RestController
@RequestMapping("/system/sys-dict-type")
public class SysDictTypeController {

    private final ISysDictTypeService dictTypeService;

    @Autowired
    public SysDictTypeController(ISysDictTypeService dictTypeService) {
        this.dictTypeService = dictTypeService;
    }

    @RequiresPermissions("sys:dict:view")
    @RequestMapping("/list")
    @Log("获取字典类型列表")
    public R list(@RequestParam Map<String, Object> params, @RequestBody SysDictType dictType) {
        IPage<SysDictType> sysDictTypeIPage = dictTypeService.selectPage(params, dictType);
        return R.ok().data(sysDictTypeIPage);
    }

    @RequiresPermissions("sys:dict:add")
    @RequestMapping("/add")
    @Log("添加字典类型")
    public R add(@RequestBody SysDictType dictType) {
        dictType.setCreateTime(LocalDateTime.now());
        return R.ok().data(dictTypeService.save(dictType));
    }

    @RequiresPermissions("sys:dict:update")
    @RequestMapping("/update")
    @Log("更新字典类型")
    public R update(@RequestBody SysDictType dictType) {
        dictType.setUpdateTime(LocalDateTime.now());
        return R.ok().data(dictTypeService.updateById(dictType));
    }

    @RequiresPermissions("sys:dict:del")
    @RequestMapping("/delete")
    @Log("删除字典类型")
    public R delete(@RequestBody SysDictType dictType) {
        dictType.setUpdateTime(LocalDateTime.now());
        return R.ok().data(dictTypeService.removeById(dictType.getId()));
    }
}
