package com.office2easy.service.system.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class RoleVo {
    private String id;
    private String name;
    private String describe;
    private int status;
    private String creatorId;
    private LocalDateTime createTime;
    private int deleted;
    private List<PermissionVo> permissions;
}
