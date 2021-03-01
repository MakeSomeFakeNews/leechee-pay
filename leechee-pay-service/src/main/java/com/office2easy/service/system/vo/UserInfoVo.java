package com.office2easy.service.system.vo;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class UserInfoVo {
    private int id;
    private String name;
    private String username;
    private String avatar;
    private int status;
    private String telephone;
    private String createId;
    private LocalDateTime creteTime;
    private int deleted;
    private String roleId;
    private RoleVo role;

}
