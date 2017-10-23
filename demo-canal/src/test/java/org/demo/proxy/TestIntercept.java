package org.demo.proxy;

import com.demo.proxy.CanalIntercept;
import com.demo.proxy.InterceptProxyObject;

public class TestIntercept implements CanalIntercept {

	public void beforeIntercept(InterceptProxyObject target) {
		System.out.println("方法开始调用前拦截-----------");
	}

	public void afterIntercept(InterceptProxyObject target) {
		// TODO Auto-generated method stub
		System.out.println("方法结束后拦截-----------");
	}

}
