package com.office2easy.system.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.office2easy.common.annotation.Log;
import com.office2easy.common.entity.R;
import com.office2easy.domain.system.SysDict;
import com.office2easy.service.system.service.ISysDictService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/system/sys-dict")
public class SysDictController {

    private final ISysDictService dictService;

    @Autowired
    public SysDictController(ISysDictService dictService) {
        this.dictService = dictService;
    }

    @RequestMapping("/list")
    @Log("获取字典列表")
    public R list(@RequestParam Map<String, Object> params, @RequestBody SysDict dict) {
        IPage<SysDict> sysDictIPage = dictService.selectPage(params, dict);
        return R.ok().data(sysDictIPage);
    }

    @RequestMapping("/add")
    @Log("添加字典")
    public R add(@RequestBody SysDict dict) {
        log.info("dict:{}", dict);
        dict.setCreateTime(LocalDateTime.now());
        return R.ok().data(dictService.save(dict));
    }

    @RequestMapping("/delete")
    @Log("删除字典")
    public R delete(@RequestBody SysDict dict) {
        return R.ok().data(dictService.removeById(dict.getId()));
    }

    @RequestMapping("/update")
    @Log("更新字典")
    public R update(@RequestBody SysDict dict) {
        return R.ok().data(dictService.updateById(dict));
    }
}
