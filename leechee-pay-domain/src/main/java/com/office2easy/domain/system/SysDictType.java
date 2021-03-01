package com.office2easy.domain.system;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author 今天太阳真滴晒
 * @since 2021-02-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value="SysDictType对象", description="")
public class SysDictType extends Model {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String dictName;

    private String dictCode;

    private String descript;

    private Integer status;

    private LocalDateTime createBy;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;


}
