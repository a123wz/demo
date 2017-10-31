package com.demo.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 
 * @author liujianquan
 * 拦截过滤
 */
public class CglibProxy implements MethodInterceptor {
	
	public List<CanalIntercept> list = new ArrayList<CanalIntercept>();
	
	public void addIntercept(CanalIntercept intercept){
		list.add(intercept);
	}
	
	public void removeIntercept(CanalIntercept intercept){
		list.add(intercept);
	}
	
	private void before(InterceptProxyObject object){
		for(CanalIntercept t:list){
			t.beforeIntercept(object);
		}
	}
	
	private void after(InterceptProxyObject object){
		for(CanalIntercept t:list){
			t.afterIntercept(object);
		}
	}
	
    public Object intercept(Object o, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {  
    	InterceptProxyObject object=new InterceptProxyObject(o, method, args, methodProxy);
        before(object);
//    	  System.out.println("++++++before " + methodProxy.getSuperName() + "++++++");  
//        System.out.println(method.getName());  
        Object o1 = methodProxy.invokeSuper(o, args);  
//        System.out.println("++++++before " + methodProxy.getSuperName() + "++++++");
        after(object);
        return o1;
    }  
}
