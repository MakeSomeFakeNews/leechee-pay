package com.office2easy.service.system.vo;

import com.office2easy.domain.system.SysUser;
import lombok.Data;

@Data
public class AuthVo extends SysUser {
    private String code;
    private String ctoken;
}
