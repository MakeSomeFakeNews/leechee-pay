package com.office2easy.service.system.vo;

import lombok.Data;

@Data
public class UserNavVo {
    private String name;
    private int parentId;
    private int id;
    private String component;
    private String redirect;
    private MetaVo meta;
    private String path;
}
