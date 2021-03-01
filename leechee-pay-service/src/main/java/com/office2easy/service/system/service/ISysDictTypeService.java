package com.office2easy.service.system.service;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.office2easy.domain.system.SysDictType;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-27
 */
public interface ISysDictTypeService extends IService<SysDictType> {

    IPage<SysDictType> selectPage(Map<String, Object> params, SysDictType dictType);
}
