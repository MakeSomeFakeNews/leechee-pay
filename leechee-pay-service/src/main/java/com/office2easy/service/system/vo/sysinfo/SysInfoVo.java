package com.office2easy.service.system.vo.sysinfo;

import lombok.Data;

@Data
public class SysInfoVo {
    private SysJvmInfo jvmInfo;
    private SysJavaInfo javaRuntimeInfo;
    private SysOsInfo osInfo;

}
