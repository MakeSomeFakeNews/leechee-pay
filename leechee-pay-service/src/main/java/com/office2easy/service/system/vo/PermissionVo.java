package com.office2easy.service.system.vo;

import lombok.Data;

import java.util.List;

@Data
public class PermissionVo {
    private String roleId;
    private String permissionId;
    private String permissionName;
    private List<ActionEntitySetVo> actionEntitySet;
}
