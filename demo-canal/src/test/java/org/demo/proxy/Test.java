package org.demo.proxy;

import org.springframework.beans.factory.annotation.Autowired;

import com.demo.proxy.CglibProxy;

import net.sf.cglib.proxy.Enhancer;

public class Test {
	@Autowired
    CglibProxy cglibProxy = null;
	public static void main(String[] args) {
        CglibProxy cglibProxy = new CglibProxy();
        TestIntercept t=new TestIntercept();
        cglibProxy.addIntercept(t);
//        cglibProxy.addIntercept(t);
        System.out.println(UserServiceImpl.class.isAssignableFrom(UserService.class));
        System.out.println(UserService.class.isAssignableFrom(UserServiceImpl.class));
        
        System.out.println(UserServiceImpl.class.getInterfaces()[0].getName());
        
        Enhancer enhancer = new Enhancer();  
        enhancer.setSuperclass(UserServiceImpl.class);  
        enhancer.setCallback(cglibProxy);  
  
        UserService o = (UserService)enhancer.create();  
        o.getName();  
//        o.getAge();  
    }  
}
