package com.office2easy.system.aspect;

import org.apache.shiro.authz.AuthorizationException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

//@Aspect
//@Component
//@Slf4j
public class DevAspect {

    @Pointcut("execution(public * com.office2easy.pay.controller..*.*(..))")
    public void pointcut() {
    }

    @Around("pointcut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String name = method.getName();
        if (name.startsWith("save") || name.startsWith("add") || name.startsWith("update") || name.startsWith("delete")) {
            throw new AuthorizationException("演示环境,禁止操作");
        }
        return point.proceed();
    }

}
