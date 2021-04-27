package com.zywl.opendubbbo;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.util.ClassUtils;

import java.lang.reflect.Method;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年04月26日 17:03:00
 */
public class DubbboMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        System.out.println("方法名称:"+method.getName());
        System.out.println(ClassUtils.isUserLevelMethod(method));
//        if ("toString".equals(method.getName())) {
//            return "";
//        }
//        if ("hashCode".equalsIgnoreCase(method.getName())) {
//            return "";
//        }
//        Object result = invocation.proceed();
        return "";
    }
}
