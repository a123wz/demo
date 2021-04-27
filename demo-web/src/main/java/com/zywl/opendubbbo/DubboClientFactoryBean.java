package com.zywl.opendubbbo;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年04月26日 16:16:00
 */
@Data
public class DubboClientFactoryBean
        implements FactoryBean<Object>{
//, InitializingBean, ApplicationContextAware
    private Class<?> type;

    @Override
    public Object getObject() throws Exception {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setInterfaces(type);
        proxyFactory.addAdvice(new DubbboMethodInterceptor());
        return proxyFactory.getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return type;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
