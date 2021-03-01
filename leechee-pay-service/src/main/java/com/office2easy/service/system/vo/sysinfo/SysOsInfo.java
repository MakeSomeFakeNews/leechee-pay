package com.office2easy.service.system.vo.sysinfo;

import lombok.Data;

@Data
public class SysOsInfo {
    private String osName;
    private String osArch;
    private String osVersion;
    private String osHostName;
    private String osHostAddress;

}
