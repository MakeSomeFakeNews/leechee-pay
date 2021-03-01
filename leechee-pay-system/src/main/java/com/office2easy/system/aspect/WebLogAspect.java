package com.office2easy.system.aspect;


import com.office2easy.common.annotation.Log;
import com.office2easy.common.utils.HttpContextUtils;
import com.office2easy.common.utils.IPUtils;
import com.office2easy.common.utils.UserUtils;
import com.office2easy.domain.system.SysLog;
import com.office2easy.service.system.service.ISysLogService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

@Aspect
@Component
@Slf4j
public class WebLogAspect {

    private final UserUtils userUtils;

    private final ISysLogService logService;

    @Autowired
    public WebLogAspect(UserUtils userUtils, ISysLogService logService) {
        this.userUtils = userUtils;
        this.logService = logService;
    }

    @Pointcut("@annotation(com.office2easy.common.annotation.Log)")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        long beginTime = System.currentTimeMillis();
        Object proceed = point.proceed();
        long time = System.currentTimeMillis() - beginTime;
        saveLog(point, time);
        return proceed;
    }

    private void saveLog(ProceedingJoinPoint joinPoint, long time) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        SysLog sysLog = new SysLog();
        Log logAnnotation = method.getAnnotation(Log.class);
        if (logAnnotation != null) {
            sysLog.setOperation(logAnnotation.value());
        }
        Object[] args = joinPoint.getArgs();
        LocalVariableTableParameterNameDiscoverer u = new LocalVariableTableParameterNameDiscoverer();
        String[] paramNames = u.getParameterNames(method);
        if (args != null && paramNames != null) {
            String params = "";
            for (int i = 0; i < args.length; i++) {
                params += "  " + paramNames[i] + ": " + args[i];
            }
            sysLog.setParams(params);
        }
        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        sysLog.setIp(IPUtils.getIpAddr(request));
        sysLog.setLink(request.getRequestURL().toString());
        sysLog.setUsername(userUtils.getUser().getUsername());
        sysLog.setExecTime((int) time);
        sysLog.setCreateTime(LocalDateTime.now());
        logService.save(sysLog);
    }
}
