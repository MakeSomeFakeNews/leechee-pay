package com.office2easy.service.system.vo.sysinfo;

import lombok.Data;

@Data
public class SysJavaInfo {
    private String jvmName;
    private String jvmVersion;
    private String jvmVendor;
    private String javaName;
    private String javaVersion;
}
